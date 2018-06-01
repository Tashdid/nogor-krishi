package com.niil.nogor.krishi.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.AppConfig;
import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;

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

	@ResponseBody
	@RequestMapping("/nurseries")
	public Map<Object, List<Nursery>> nurseries() {
		return nurseryRepo.findAll().stream().collect(Collectors.groupingBy(n -> n.getArea().getId(), Collectors.toList()));
	}

}
