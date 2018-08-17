package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.MaterialType;
import com.niil.nogor.krishi.repo.MaterialTypeRepo;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(MaterialTypeController.URL)
public class MaterialTypeController {
	static final String URL = "/manage/materialtype";

	@Autowired MaterialTypeRepo materialTypeRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		MaterialType bean = code == null ? new MaterialType() : materialTypeRepo.findOne(code);
		if (bean == null) bean = new MaterialType();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Material Type");
		model.addAttribute("allbeans", materialTypeRepo.findAllByOrderBySequenceAsc());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid MaterialType type) throws IOException {
		MaterialType bean;
		if (type.getId() == null || (bean = materialTypeRepo.findOne(type.getId())) == null) {
			MaterialType lt = materialTypeRepo.findTopByOrderBySequenceDesc();
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = MaterialType.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean = materialTypeRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			materialTypeRepo.save(updater.getData().entrySet().stream().map(e -> {
				MaterialType bean = materialTypeRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		materialTypeRepo.delete(code);
		return true;
	}
}


