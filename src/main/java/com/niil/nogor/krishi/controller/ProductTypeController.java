package com.niil.nogor.krishi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/manage/type")
public class ProductTypeController {
	@Autowired ProductTypeRepo productTypeRepo;

	@ModelAttribute("types")
	public List<ProductType> productTypeRepo() {
		return (ArrayList<ProductType>) productTypeRepo.findAll();
	}

	@RequestMapping
	public String loadNew() {
		return "producttype";
	}
}
