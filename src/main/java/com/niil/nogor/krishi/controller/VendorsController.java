package com.niil.nogor.krishi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.entity.Area;
import com.niil.nogor.krishi.entity.DemographicData;
import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.ProductPrice;
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
	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired DemographicDataRepo demographicDataRepo;

	@RequestMapping({"/vendors", "/vendors/{cityy}"})
	public String vendors(@PathVariable(required=false) DemographicData cityy, final ModelMap model) {
//		List<Area> areas = areaRepo.findAllByOrderBySequenceAsc();
//		model.addAttribute("areas", areas);
//		Area ar = area == null ? areas.get(0) : area;
//		model.addAttribute("area", ar);
		if(cityy != null)
			model.addAttribute("vendors", nurseryRepo.findAllByCityOrderBySequenceAsc(cityy));
		else
			model.addAttribute("vendors", nurseryRepo.findAll());
		
		System.out.println(nurseryRepo.findAllByCityOrderBySequenceAsc(cityy));
		
		List<DemographicData>bivaggulo=demographicDataRepo.findAllByParentIdIsNullOrderByNameAsc();
		List<DemographicData> zelagulo= new ArrayList<DemographicData>();
		if(bivaggulo!=null && !bivaggulo.isEmpty()) {
			zelagulo=demographicDataRepo.findAllByParentIdOrderByNameAsc(bivaggulo.get(0).getId());
		}
		
		model.addAttribute("bivaggulo", bivaggulo);
		model.addAttribute("zelagulo", zelagulo);
		model.addAttribute("upozelagulo", zelagulo!=null && !zelagulo.isEmpty()?demographicDataRepo.findAllByParentIdOrderByNameAsc(zelagulo.get(0).getId()):new ArrayList<>());

		
		
		return "site/vendors";
	}

	@RequestMapping("/vendor/{vendor}")
	public String vendorDetails(@PathVariable Nursery vendor, final ModelMap model) {
		List<Area> areas = areaRepo.findAllByOrderBySequenceAsc();
		model.addAttribute("areas", areas);
		model.addAttribute("area", vendor.getArea());
		model.addAttribute("vendor", vendor);
		model.addAttribute("products", productPriceRepo.findAllByNursery(vendor).stream().map(ProductPrice::getProduct).distinct().collect(Collectors.groupingBy(p -> p.getType())));
		model.addAttribute("materials", materialPriceRepo.findAllByNursery(vendor));
		model.addAttribute("type", productTypeRepo.findAll().stream().filter(t -> t.isLinkedToMaterial()).findFirst().orElse(null));
		return "site/vendor";
	}
}
