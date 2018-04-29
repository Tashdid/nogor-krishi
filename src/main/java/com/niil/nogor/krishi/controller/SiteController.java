package com.niil.nogor.krishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.repo.ProductTypeRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 29, 2018
 *
 */
@Controller
@RequestMapping
public class SiteController {
	@Autowired
	ProductTypeRepo productTypeRepo;

	@RequestMapping
	public String homePage(final ModelMap model) {
		model.addAttribute("types", productTypeRepo.findAllByOrderBySequenceAsc());
		return "site/index";
	}

	@RequestMapping("/ponno")
	public String ponnoPage() {
		return "site/ponno";
	}
}
