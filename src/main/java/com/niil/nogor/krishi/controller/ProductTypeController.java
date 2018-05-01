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
import com.niil.nogor.krishi.view.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping("/manage/producttype")
public class ProductTypeController {

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
		model.addAttribute("beans", productTypeRepo.findAllByOrderBySequenceAsc());
		return "manage/producttype";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid ProductTypeForm type) throws IOException {
		ProductType pt;
		if (type.getId() == null || (pt = productTypeRepo.findOne(type.getId())) == null) {
			pt = ProductType.builder()
						.name(type.getName())
						.alternativeName(type.getAlternativeName())
						.sequence(productTypeRepo.findTopByOrderBySequenceDesc().getSequence() + 1)
						.build();
		}
		pt.setIcon(type.getIconFile() == null || type.getIconFile().isEmpty() ? pt.getIcon() : type.getIconFile().getBytes());
		pt.setImage(type.getImageFile() == null || type.getImageFile().isEmpty() ? pt.getImage() : type.getImageFile().getBytes());
		pt = productTypeRepo.save(pt);
		return "redirect:/manage/producttype/" + pt.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			productTypeRepo.save(updater.getData().entrySet().stream().map(e -> {
				ProductType pt = productTypeRepo.findOne(e.getKey());
				pt.setSequence(e.getValue());
				return pt;
			}).collect(Collectors.toList()));
		}
		return true;
	}
}


