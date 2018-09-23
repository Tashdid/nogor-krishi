package com.niil.nogor.krishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.entity.MaterialPrice;
import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.repo.MaterialPriceRepo;
import com.niil.nogor.krishi.repo.NurseryRepo;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.service.SecurityService;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 22, 2018
 *
 */
@Controller
@RequestMapping(VendorPriceController.URL)
public class VendorPriceController {
	static final String URL = "/vendorprice";

	@Autowired NurseryRepo nurseryRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired SecurityService securityService;
	@Autowired NurseryController nurserController;

	@GetMapping
	public String vendorPricePage(final ModelMap model) {
		model.addAttribute("basePath", URL + "/");
		Nursery bean = securityService.findLoggedInUser().getNursery();
		model.addAttribute("bean", bean);
		nurserController.loadNurseryPrices(bean, model);
		return URL.substring(1);
	}

	@RequestMapping(value="/{code}/product", method=RequestMethod.POST)
	public String addProductPrice(@PathVariable Long code, ProductPrice mprice, RedirectAttributes redirectAttrs) {
		nurserController.addProductPrice(code, mprice, redirectAttrs);
		return "redirect:" + URL;
	}

	@RequestMapping(value="/{code}/product/{product}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteProductPrice(@PathVariable Long code, @PathVariable Long product) {
		return nurserController.deleteProductPrice(code, product);
	}

	@RequestMapping(value="/{code}/material", method=RequestMethod.POST)
	public String addMaterialPrice(@PathVariable Long code, MaterialPrice mprice, RedirectAttributes redirectAttrs) {
		nurserController.addMaterialPrice(code, mprice, redirectAttrs);
		return "redirect:" + URL;
	}

	@RequestMapping(value="/{code}/material/{material}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteMaterialPrice(@PathVariable Long code, @PathVariable Long material) {
		return nurserController.deleteMaterialPrice(code, material);
	}
}
