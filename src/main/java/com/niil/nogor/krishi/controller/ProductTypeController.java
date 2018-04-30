package com.niil.nogor.krishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.entity.ProductType;
import com.niil.nogor.krishi.repo.ProductTypeRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping("/manage/producttype")
public class ProductTypeController {

	@Autowired ProductTypeRepo productTypeRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		ProductType bean = code == null ? new ProductType() : productTypeRepo.findOne(code);
		if (bean == null) bean = new ProductType();
		model.addAttribute("bean", bean);
		model.addAttribute("beans", productTypeRepo.findAll());
		return "manage/producttype";
	}
}


