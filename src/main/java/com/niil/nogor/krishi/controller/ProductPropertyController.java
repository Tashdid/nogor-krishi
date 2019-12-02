package com.niil.nogor.krishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.repo.ProductPropertyRepo;

@Controller
@RequestMapping(ProductPropertyController.URL)
public class ProductPropertyController extends AbstractController{
	
	static final String URL = "/manage/productproperty";
	
	@Autowired ProductPropertyRepo productPropertyRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		//return updateScreen(null, model);
		return "";
	}
	
	

	
	

}
