package com.niil.nogor.krishi.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.Material;
import com.niil.nogor.krishi.repo.MaterialRepo;
import com.niil.nogor.krishi.repo.MaterialTypeRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(MaterialController.URL)
public class MaterialController extends AbstractController {
	static final String URL = "/manage/material";

	@Autowired MaterialRepo materialRepo;
	@Autowired MaterialTypeRepo materialTypeRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		Material bean = code == null ? new Material() : materialRepo.findOne(code);
		if (bean == null) bean = new Material();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Material");
		model.addAttribute("allbeans", materialRepo.findAll());
		model.addAttribute("materialtypes", materialTypeRepo.findAll());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Material type) throws IOException {
		type = materialRepo.save(type);
		return "redirect:" + URL + "/" + type.getId();
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		materialRepo.delete(code);
		return true;
	}
}


