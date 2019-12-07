package com.niil.nogor.krishi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niil.nogor.krishi.entity.ProductPropertyMapping;
import com.niil.nogor.krishi.repo.ProductPropertyRepo;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.ProductPropertyMappingRepo;

@Controller
@RequestMapping(ProductPropertyMappingController.URL)
public class ProductPropertyMappingController extends AbstractController{
	
	static final String URL = "/manage/productpropertymapping";
	
	@Autowired ProductPropertyMappingRepo productPropertyMappingRepo;
	@Autowired ProductRepo productRepo;
	@Autowired ProductPropertyRepo productPropertyRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}
	
	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		ProductPropertyMapping bean = code == null ? new ProductPropertyMapping() : productPropertyMappingRepo.findOne(code);
		if (bean == null) bean = new ProductPropertyMapping();
		model.addAttribute("bean", bean);
		model.addAttribute("allbeans", productPropertyMappingRepo.findAll());
		model.addAttribute("allproducts", productRepo.findAllAsChildOnly());
		model.addAttribute("allproperties", productPropertyRepo.findAll());
		return URL.substring(1);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String save(@Valid ProductPropertyMapping bean) {
		productPropertyMappingRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}
	
	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		productPropertyMappingRepo.delete(code);
		return true;
	}

}
