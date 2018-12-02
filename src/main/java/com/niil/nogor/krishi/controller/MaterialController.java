package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niil.nogor.krishi.entity.Material;
import com.niil.nogor.krishi.repo.MaterialRepo;
import com.niil.nogor.krishi.repo.MaterialTypeRepo;
import com.niil.nogor.krishi.view.MaterialForm;
import com.niil.nogor.krishi.view.SequenceUpdater;

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
	public String save(@Valid MaterialForm material) throws IOException {
		Material bean;
		if (material.getId() == null || (bean = materialRepo.findOne(material.getId())) == null) {
			Material lt = materialRepo.findTopByOrderBySequenceDesc();
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = Material.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(material.getName());
		bean.setDescription(material.getDescription());
		bean.setType(material.getType());
		
		bean.setImage(material.getImageFile() == null || material.getImageFile().isEmpty() ? bean.getImage() : material.getImageFile().getBytes());
		
		bean = materialRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater == null) return true;
		materialRepo.save(updater.getData().entrySet().stream().map(e -> {
			Material bean = materialRepo.findOne(e.getKey());
			bean.setSequence(e.getValue());
			return bean;
		}).collect(Collectors.toList()));
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		materialRepo.delete(code);
		return true;
	}
}


