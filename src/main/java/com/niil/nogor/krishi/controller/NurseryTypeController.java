package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.NurseryType;
import com.niil.nogor.krishi.repo.NurseryTypeRepo;
import com.niil.nogor.krishi.view.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(NurseryTypeController.URL)
public class NurseryTypeController {
	static final String URL = "/manage/nurserytype";

	@Autowired NurseryTypeRepo nurseryTypeRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		NurseryType bean = code == null ? new NurseryType() : nurseryTypeRepo.findOne(code);
		if (bean == null) bean = new NurseryType();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Nursery Type");
		model.addAttribute("beans", nurseryTypeRepo.findAllByOrderBySequenceAsc());
		return URL;
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid ProductTypeForm type) throws IOException {
		NurseryType bean;
		if (type.getId() == null || (bean = nurseryTypeRepo.findOne(type.getId())) == null) {
			NurseryType lt = nurseryTypeRepo.findTopByOrderBySequenceDesc();
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = NurseryType.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean.setImage(type.getImageFile() == null || type.getImageFile().isEmpty() ? bean.getImage() : type.getImageFile().getBytes());
		bean = nurseryTypeRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			nurseryTypeRepo.save(updater.getData().entrySet().stream().map(e -> {
				NurseryType bean = nurseryTypeRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		nurseryTypeRepo.delete(code);
		return true;
	}
}