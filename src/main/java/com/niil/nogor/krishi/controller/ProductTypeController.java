package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.ProductType;
import com.niil.nogor.krishi.repo.ProductTypeRepo;
import com.niil.nogor.krishi.view.ProductTypeForm;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(ProductTypeController.URL)
public class ProductTypeController {
	static final String URL = "/manage/producttype";

	@Autowired ProductTypeRepo productTypeRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		ProductType bean = code == null ? new ProductType() : productTypeRepo.findOne(code);
		if (bean == null) bean = new ProductType();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Product Type");
		model.addAttribute("beans", productTypeRepo.findAllByOrderBySequenceAsc());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid ProductTypeForm type) throws IOException {
		ProductType bean;
		if (type.getId() == null || (bean = productTypeRepo.findOne(type.getId())) == null) {
			ProductType lt = productTypeRepo.findTopByOrderBySequenceDesc();
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = ProductType.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean.setAlternativeName(type.getAlternativeName());
		bean.setIcon(type.getIconFile() == null || type.getIconFile().isEmpty() ? bean.getIcon() : type.getIconFile().getBytes());
		bean.setImage(type.getImageFile() == null || type.getImageFile().isEmpty() ? bean.getImage() : type.getImageFile().getBytes());
		bean = productTypeRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			productTypeRepo.save(updater.getData().entrySet().stream().map(e -> {
				ProductType bean = productTypeRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		productTypeRepo.delete(code);
		return true;
	}
}