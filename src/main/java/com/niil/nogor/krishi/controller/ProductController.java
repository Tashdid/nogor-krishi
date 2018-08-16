package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.Material;
import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.repo.*;
import com.niil.nogor.krishi.view.ProductForm;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(ProductController.URL)
public class ProductController {
	static final String URL = "/manage/product";

	@Autowired SaleTypeRepo saleTypeRepo;
	@Autowired ProductRepo productRepo;
	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired MaterialRepo materialRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		Product bean = code == null ? new Product() : productRepo.findOne(code);
		if (bean == null) bean = new Product();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Product");
		model.addAttribute("beans", productRepo.findAll());
		model.addAttribute("types", productTypeRepo.findAllByOrderBySequenceAsc());
		model.addAttribute("saletypes", saleTypeRepo.findAll());
		List<Material> exMats = bean.getMaterials() == null ? new ArrayList<>() : bean.getMaterials();
		model.addAttribute("materials", ((ArrayList<Material>) materialRepo.findAll()).stream().filter(mt -> exMats.stream().noneMatch(pmt -> pmt.getId().equals(mt.getId()))).collect(Collectors.toList()));
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid ProductForm product) throws IOException {
		Product bean;
		if (product.getId() == null || (bean = productRepo.findOne(product.getId())) == null) {
			Product lt = productRepo.findTopByTypeOrderBySequenceDesc(product.getType());
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = Product.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(product.getName());
		bean.setAlternativeName(product.getAlternativeName());
		bean.setDescription(product.getDescription());
		bean.setUsefulVarieties(product.getUsefulVarieties());
		bean.setSuitableTime(product.getSuitableTime());
		bean.setLandPreparation(product.getLandPreparation());
		bean.setSeedlingType(product.getSeedlingType());
		bean.setCareness(product.getCareness());
		bean.setPlace(product.getPlace());
		bean.setHormone(product.getHormone());
		bean.setPesticides(product.getPesticides());
		bean.setCaution(product.getCaution());
		bean.setProductivity(product.getProductivity());
		bean.setBenefits(product.getBenefits());
		bean.setType(product.getType());
		bean.setSaleType(product.getSaleType());

		bean.setIcon(product.getIconFile() == null || product.getIconFile().isEmpty() ? bean.getIcon() : product.getIconFile().getBytes());
		bean.setImage(product.getImageFile() == null || product.getImageFile().isEmpty() ? bean.getImage() : product.getImageFile().getBytes());

		bean = productRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			productRepo.save(updater.getData().entrySet().stream().map(e -> {
				Product bean = productRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		productRepo.delete(code);
		return true;
	}

	@RequestMapping(value="/{code}/material", method=RequestMethod.POST)
	public String addMaterial(@PathVariable Long code, @RequestParam Long material) {
		Product bean = productRepo.findOne(code);
		Material mt = materialRepo.findOne(material);
		bean.getMaterials().add(mt);
		productRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/{code}/material/{material}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteMaterial(@PathVariable Long code, @PathVariable Long material) {
		Product bean = productRepo.findOne(code);
		Optional<Material> mtop = bean.getMaterials().stream().filter(mt -> mt.getId().equals(material)).findFirst();
		if (mtop.isPresent()) {
			bean.getMaterials().remove(mtop.get());
			productRepo.save(bean);
		}
		return true;
	}
}


