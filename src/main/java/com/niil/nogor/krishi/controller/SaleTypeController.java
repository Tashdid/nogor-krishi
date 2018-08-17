package com.niil.nogor.krishi.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.SaleType;
import com.niil.nogor.krishi.repo.SaleTypeRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(SaleTypeController.URL)
public class SaleTypeController {
	static final String URL = "/manage/saletype";

	@Autowired SaleTypeRepo saleTypeRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		SaleType bean = code == null ? new SaleType() : saleTypeRepo.findOne(code);
		if (bean == null) bean = new SaleType();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Sale Type");
		model.addAttribute("allbeans", saleTypeRepo.findAll());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid SaleType type) throws IOException {
		type = saleTypeRepo.save(type);
		return "redirect:" + URL + "/" + type.getId();
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		saleTypeRepo.delete(code);
		return true;
	}
}


