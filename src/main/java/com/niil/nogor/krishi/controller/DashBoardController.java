package com.niil.nogor.krishi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niil.nogor.krishi.entity.ProductType;
import com.niil.nogor.krishi.repo.*;
import com.niil.nogor.krishi.view.EngToBengali;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 15, 2018
 *
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(DashBoardController.URL)
public class DashBoardController extends AbstractController {
	static final String URL = "/dashboard";

	@Autowired EngToBengali e2bconv;
	@Autowired ProductRepo productRepo;
	@Autowired MaterialRepo materialRepo;
	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired NurseryTypeRepo nurseryTypeRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;

	@RequestMapping
	public String dashboardPage(ModelMap model) {
		return URL.substring(1);
	}

	@RequestMapping(value="/ponno", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> ponnoData() {
		Map<String, Object> response = new HashMap<>();
		List<ProductType> types = productTypeRepo.findAll();
		long total = productRepo.count();
		boolean isMat = types.stream().anyMatch(t -> t.isLinkedToMaterial());
		if (isMat)
			total += materialRepo.count();
		List<String> labels = new ArrayList<>();
		List<Long> values = new ArrayList<>();
		types.forEach(t -> {
			long qn = t.isLinkedToMaterial() ? materialRepo.count() : productRepo.findAllByTypeOrderBySequenceAsc(t).size();
			labels.add(t.getName() + " (" + e2bconv.getBengali(qn) + ")");
			values.add(qn);
		});
		response.put("total", e2bconv.getBengali(total));
		response.put("labels", labels);
		response.put("data", values);
		return response;
	}

	@RequestMapping(value="/nursery", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> nurseryData() {
		Map<String, Object> response = new HashMap<>();
		List<String> labels = new ArrayList<>();
		List<Long> data1 = new ArrayList<>();
		List<Long> data2 = new ArrayList<>();
		nurseryRepo.findAll().stream().sorted((o1, o2) -> Integer.compare(o1.getType().getSequence(), o2.getType().getSequence())).forEach(n -> {
			data1.add(productPriceRepo.findAllByNursery(n).stream().map(p -> p.getProduct().getId()).distinct().count());
			data2.add(materialPriceRepo.findAllByNursery(n).stream().map(p -> p.getMaterial().getId()).distinct().count());
			labels.add(n.getName());
		});
		response.put("data1", data1);
		response.put("data2", data2);
		response.put("labels", labels);
		return response;
	}

}
