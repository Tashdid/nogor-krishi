package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.City;
import com.niil.nogor.krishi.repo.CityRepo;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(CityController.URL)
public class CityController {
	static final String URL = "/manage/city";

	@Autowired CityRepo cityRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		City bean = code == null ? new City() : cityRepo.findOne(code);
		if (bean == null) bean = new City();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "City");
		model.addAttribute("beans", cityRepo.findAllByOrderBySequenceAsc());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid City type) throws IOException {
		City bean;
		if (type.getId() == null || (bean = cityRepo.findOne(type.getId())) == null) {
			City lt = cityRepo.findTopByOrderBySequenceDesc();
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = City.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean = cityRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			cityRepo.save(updater.getData().entrySet().stream().map(e -> {
				City bean = cityRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		cityRepo.delete(code);
		return true;
	}
}


