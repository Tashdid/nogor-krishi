package com.niil.nogor.krishi.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;
import com.niil.nogor.krishi.service.SecurityService;
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
	private static final String ERROR_PAGE = "error";
	private static final String LAYOUT_NOT_FOUND = "দুঃখিত, বাগান লে-আউট পাওয়া যায় নাই!";

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
	@Autowired SecurityService securityService;

	@RequestMapping
	public String homePage(final ModelMap model) {
		List<ProductType> types = productTypeRepo.findAllByOrderBySequenceAsc();
		model.addAttribute("types", types);
		List<Area> areas = areaRepo.findAll();
		model.addAttribute("areas", areas);
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
		model.addAttribute("products", type == null ? productRepo.findAll() : productRepo.findAllByTypeOrderBySequenceAsc(type));
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
			typeProducts(types.get(0), model);
		}
		return "site/layout";
	}

	@ResponseBody
	@RequestMapping("/nurseries")
	public Map<Object, List<Nursery>> nurseries() {
		Map<Object, List<Nursery>> nurseries = nurseryRepo.findAll().stream().collect(Collectors.groupingBy(n -> n.getArea().getId(), Collectors.toList()));
		return nurseries;
	}

	@RequestMapping("/layout/items/{type}")
	public String typeProducts(@PathVariable ProductType type, final ModelMap model) {
		List<Product> list = productRepo.findAllByTypeOrderBySequenceAsc(type);
		list.forEach(p -> p.setImage(null));
		model.addAttribute("products", list);
		return "site/layout :: products";
	}

	@RequestMapping(value="/layout", method=RequestMethod.POST)
	public String layoutSave(@Valid LayoutRQ layout, final ModelMap model,
			HttpServletResponse response, final RedirectAttributes redirectAttrs) {
		GardenLayout gl = new GardenLayout();
		gl.setLength(layout.getLength());
		gl.setWidth(layout.getWidth());
		gl.setImage(Base64.getDecoder().decode(layout.getEncodedImage()));
		GardenLayout sgl = gardenLayoutRepo.save(gl);
		layout.getBlocks().forEach(bl -> {
			bl.setGardenLayout(sgl);
			gardenBlockRepo.save(bl);
		});

		User user = securityService.findLoggedInUser();
		if (user == null) {
			Cookie foo = new Cookie("lastLayout", sgl.getId().toString());
			foo.setMaxAge(3600); //set expire time to 1 hour
			response.addCookie(foo);
		} else {
			sgl.setUser(user);
			gardenLayoutRepo.save(sgl);
		}
		redirectAttrs.addFlashAttribute("newlayout", true);
		return "redirect:/exlayout/" + sgl.getId();
	}

	@RequestMapping(value="/exlayout/{layout}")
	public String showLayout(@PathVariable(required=false) GardenLayout layout,
			@CookieValue(value = "lastLayout", required=false) String lastLayout,
			HttpServletResponse response, final ModelMap model) {
		if (layout == null) {
			model.addAttribute("msg", LAYOUT_NOT_FOUND);
			return ERROR_PAGE;
		}
		model.addAttribute("layout", layout);
		User user = securityService.findLoggedInUser();
		if (layout.getUser() == null && lastLayout != null && user != null && lastLayout.equals(layout.getId().toString())) {
			layout.setUser(user);
			gardenLayoutRepo.save(layout);
			Cookie foo = new Cookie("lastLayout", null);
			foo.setMaxAge(0);
			response.addCookie(foo);
			model.addAttribute("newlayout", true);
		} else if (layout.getUser() == null || (user != null && !user.getId().equals(layout.getUser().getId()))) {
			model.addAttribute("msg", LAYOUT_NOT_FOUND);
			return ERROR_PAGE;
		}
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

	@RequestMapping(value="/exlayout/list")
	public String myLayouts(final ModelMap model) {
		User user = securityService.findLoggedInUser();
		model.addAttribute("layouts", user == null ? Collections.emptyList() : gardenLayoutRepo.findByUserOrderByCreatedOnDesc(user));
		return "site/exlayoutlist";
	}

	@ResponseBody
	@RequestMapping(value="/exlayout/{layout}", method=RequestMethod.POST)
	public Object deleteLayout(@PathVariable(required=false) GardenLayout layout) {
		if (layout == null || !securityService.findLoggedInUser().getId().equals(layout.getUser().getId()))
			return LAYOUT_NOT_FOUND;

		gardenLayoutRepo.delete(layout);
		return true;
	}
}
