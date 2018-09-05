package com.niil.nogor.krishi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.entity.Area;
import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.repo.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jun 5, 2018
 *
 */
@Controller
@RequestMapping
public class VendorsController extends AbstractController {

	@Autowired NurseryRepo nurseryRepo;
	@Autowired AreaRepo areaRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;

	@RequestMapping({"/vendors", "/vendors/{area}"})
	public String vendors(@PathVariable(required=false) Area area, final ModelMap model) {
		List<Area> areas = areaRepo.findAllByOrderBySequenceAsc();
		model.addAttribute("areas", areas);
		Area ar = area == null ? areas.get(0) : area;
		model.addAttribute("area", ar);
		model.addAttribute("vendors", nurseryRepo.findAllByAreaOrderBySequenceAsc(ar));
		return "site/vendors" + (area == null ? "" : " :: vendors");
	}

	@RequestMapping("/vendor/{vendor}")
	public String vendorDetails(@PathVariable Nursery vendor, final ModelMap model) {
		model.addAttribute("vendor", vendor);
		model.addAttribute("products", productPriceRepo.findAllByNursery(vendor).stream().collect(Collectors.groupingBy(p -> p.getProduct().getType())));
		model.addAttribute("materials", materialPriceRepo.findAllByNursery(vendor));
		return "site/vendor";
	}
}
