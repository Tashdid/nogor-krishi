package com.niil.nogor.krishi.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.config.AppConfig;
import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;
import com.niil.nogor.krishi.view.LayoutRQ;
import com.niil.nogor.krishi.view.NurseryPrices;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 29, 2018
 *
 */
@Controller
@RequestMapping
public class SiteController {

	@Autowired AppConfig appConfig;

	private Comparator<Nursery> byNurseryType = Comparator.comparing(
			parent -> parent.getType().getSequence()
	);

	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired ProductRepo productRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired AreaRepo areaRepo;
	@Autowired GardenLayoutRepo gardenLayoutRepo;
	@Autowired GardenBlockRepo gardenBlockRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;

	@RequestMapping
	public String homePage(final ModelMap model) {
		model.addAttribute("types", productTypeRepo.findAllByOrderBySequenceAsc());
		model.addAttribute("areas", areaRepo.findAll());
		return "site/index";
	}

	@RequestMapping("/ponno")
	public String ponnoPage(@RequestParam(required=false) ProductType type, final ModelMap model) {
		List<ProductType> types = productTypeRepo.findAllByOrderBySequenceAsc();
		model.addAttribute("types", types);
		products(type, model);
		return "site/ponno";
	}

	@RequestMapping("/ponno/{type}")
	public String products(@PathVariable ProductType type, final ModelMap model) {
		model.addAttribute("type", type);
		List<Product> list = type == null ? productRepo.findAll() : productRepo.findAllByTypeOrderBySequenceAsc(type);
		if (appConfig.isCopyProductList()) {
			list.addAll(list);
			list.addAll(list);
			list.addAll(list);
		}
		model.addAttribute("products", list);
		return "site/ponno :: products";
	}

	@RequestMapping("/ponno/product/{product}")
	public String product(@PathVariable Product product, final ModelMap model) {
		model.addAttribute("product", product);
		List<ProductPrice> prices = productPriceRepo.findAllByProduct(product);
		List<Nursery> nurseries = prices.stream().map(p -> p.getNursery()).distinct()
				.sorted(byNurseryType.thenComparing(Nursery::getSequence))
				.collect(Collectors.toList());
		model.addAttribute("nurseries", nurseries);
		model.addAttribute("areas", nurseries.stream().map(n -> n.getArea()).distinct()
				.sorted(Comparator.comparing(Area::getSequence))
				.collect(Collectors.toList()));
		return "site/product";
	}

	@RequestMapping("/layout")
	public String layout(final ModelMap model) {
		List<ProductType> types = productTypeRepo.findAllByOrderBySequenceAsc();
		model.addAttribute("types", types.stream().collect(Collectors.toMap(t -> t.getId(), t -> t.getName())));
		if (!types.isEmpty()) {
			List<Product> list = productRepo.findAll();
			list.forEach(p -> p.setImage(null));
			model.addAttribute("products", list);
		}
		return "site/layout";
	}

	@RequestMapping(value="/layout", method=RequestMethod.POST)
	public String layoutSave(@Valid LayoutRQ layout, final ModelMap model) {
		GardenLayout gl = new GardenLayout();
		gl.setLength(layout.getLength());
		gl.setWidth(layout.getWidth());
		gl.setImage(Base64.getDecoder().decode(layout.getEncodedImage()));
		GardenLayout sgl = gardenLayoutRepo.save(gl);
		layout.getBlocks().forEach(bl -> {
			bl.setGardenLayout(sgl);
			gardenBlockRepo.save(bl);
		});
		return "redirect:/layout/" + sgl.getId();
	}

	@RequestMapping(value="/layout/{layout}")
	public String showLlayout(@PathVariable GardenLayout layout, final ModelMap model) {
		model.addAttribute("layout", layout);
		List<Product> prods = gardenBlockRepo.findAllByGardenLayout(layout).stream().map(b -> b.getProduct()).distinct().collect(Collectors.toList());
		List<MaterialPrice> mprices = prods.stream().flatMap(p -> p.getMaterials().stream()).distinct().flatMap(m -> materialPriceRepo.findAllByMaterial(m).stream()).collect(Collectors.toList());
		prods.stream().flatMap(p -> p.getMaterials().stream()).distinct().collect(Collectors.toMap(m -> m.getId(), m -> {
			return m;
		}));
		Map<Nursery, List<ProductPrice>> abc = new HashMap<>();
		abc.entrySet().stream().collect(Collectors.toMap(ne -> ne.getKey(), ne -> {
			NurseryPrices np = new NurseryPrices();
			np.setProductPrice(ne.getValue().get(0));
			np.setMaterialPrices(mprices.stream().filter(mp -> mp.getNursery().getId().equals(ne.getKey().getId())).collect(Collectors.toList()));
			return np;
		}));
		Map<Product, Map<Area, Map<Nursery, NurseryPrices>>> ncc= prods.stream().collect(Collectors.toMap(p -> p, p -> {
			Map<Area, Map<Nursery, NurseryPrices>> anp = productPriceRepo.findAllByProduct(p).stream().collect(Collectors.groupingBy(pp -> pp.getNursery().getArea()))
				.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> {
						Map<Nursery, NurseryPrices> npc = e.getValue().stream().collect(Collectors.groupingBy(pb -> pb.getNursery())).entrySet().stream().collect(Collectors.toMap(ne -> ne.getKey(), ne -> {
							NurseryPrices np = new NurseryPrices();
							np.setProductPrice(ne.getValue().get(0));
							np.setMaterialPrices(mprices.stream().filter(mp -> mp.getNursery().getId().equals(ne.getKey().getId())).collect(Collectors.toList()));
							return np;
						})).entrySet().stream().sorted(Map.Entry.comparingByKey(byNurseryType.thenComparing(Nursery::getSequence))).collect(Collectors.toMap(nne -> nne.getKey(), nne -> nne.getValue()));
					return npc;
			}));
			return anp;
		}));
		model.addAttribute("ncc", ncc);
		model.addAttribute("nurseries", nurseries());
		return "site/exlayout";
	}

	@ResponseBody
	@RequestMapping("/nurseries")
	public Map<Object, List<Nursery>> nurseries() {
		return nurseryRepo.findAll().stream().collect(Collectors.groupingBy(n -> n.getArea().getId(), Collectors.toList()));
	}

}
