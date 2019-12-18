package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.DemographicData;
import com.niil.nogor.krishi.repo.DemographicDataRepo;
import com.niil.nogor.krishi.view.SequenceUpdater;

@Controller
@RequestMapping(DemographicDataController.URL)
public class DemographicDataController extends AbstractController {
	static final String URL = "/manage/demographicdata";

	@Autowired DemographicDataRepo demographicDataRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		DemographicData bean = code == null ? new DemographicData() : demographicDataRepo.findOne(code);
		if (bean == null) bean = new DemographicData();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "DemographicData");
		model.addAttribute("allbeans", demographicDataRepo.findAllByOrderBySequenceAsc());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid DemographicData type) throws IOException {
		DemographicData bean;
		if (type.getId() == null || (bean = demographicDataRepo.findOne(type.getId())) == null) {
			DemographicData lt = demographicDataRepo.findTopByOrderBySequenceDesc();
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = DemographicData.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean = demographicDataRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			demographicDataRepo.save(updater.getData().entrySet().stream().map(e -> {
				DemographicData bean = demographicDataRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		demographicDataRepo.delete(code);
		return true;
	}
}


