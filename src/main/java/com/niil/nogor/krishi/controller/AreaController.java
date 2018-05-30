package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.Area;
import com.niil.nogor.krishi.repo.AreaRepo;
import com.niil.nogor.krishi.repo.CityRepo;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(AreaController.URL)
public class AreaController {
	static final String URL = "/manage/area";

	@Autowired AreaRepo areaRepo;
	@Autowired CityRepo cityRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		Area bean = code == null ? new Area() : areaRepo.findOne(code);
		if (bean == null) bean = new Area();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Area");
		model.addAttribute("beans", areaRepo.findAll());
		model.addAttribute("cities", cityRepo.findAllByOrderBySequenceAsc());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Area type) throws IOException {
		Area bean;
		if (type.getId() == null || (bean = areaRepo.findOne(type.getId())) == null) {
			Area lt = areaRepo.findTopByCityOrderBySequenceDesc(type.getCity());
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = Area.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean.setCity(type.getCity());
		bean.setLatitude(type.getLatitude());
		bean.setLongitude(type.getLongitude());
		bean = areaRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			areaRepo.save(updater.getData().entrySet().stream().map(e -> {
				Area bean = areaRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		areaRepo.delete(code);
		return true;
	}
}


