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

import com.niil.nogor.krishi.entity.City;
import com.niil.nogor.krishi.entity.DeliveryPerson;
import com.niil.nogor.krishi.repo.DeliveryPersonRepo;
import com.niil.nogor.krishi.view.SequenceUpdater;

@Controller
@RequestMapping(DeliveryPersonController.URL)
public class DeliveryPersonController extends AbstractController {
	
	static final String URL = "/nursery/deliveryperson";

	@Autowired DeliveryPersonRepo deliveryPersonRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		System.out.println("test================================");
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		DeliveryPerson bean = code == null ? new DeliveryPerson() : deliveryPersonRepo.findOne(code);
		if (bean == null) bean = new DeliveryPerson();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "DeliveryPerson");
		model.addAttribute("allbeans", deliveryPersonRepo.findAllByOrderBySequenceAsc());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid DeliveryPerson type) throws IOException {
		DeliveryPerson bean;
		
		if (type.getId() == null || (bean = deliveryPersonRepo.findOne(type.getId())) == null) {
			DeliveryPerson lt = deliveryPersonRepo.findTopByOrderBySequenceDesc();
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = DeliveryPerson.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean.setPhoneNumber(type.getPhoneNumber());
		
		bean = deliveryPersonRepo.save(bean);
		
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			deliveryPersonRepo.save(updater.getData().entrySet().stream().map(e -> {
				DeliveryPerson bean = deliveryPersonRepo.findOne(e.getKey());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		deliveryPersonRepo.delete(code);
		return true;
	}

}
