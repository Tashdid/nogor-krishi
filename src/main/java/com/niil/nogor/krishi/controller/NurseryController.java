package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Controller
@RequestMapping(NurseryController.URL)
public class NurseryController {
	static final String URL = "/manage/nursery";

	@Autowired AreaRepo areaRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired NurseryTypeRepo nurseryTypeRepo;
	@Autowired MaterialRepo materialRepo;
	@Autowired ProductRepo productRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, final ModelMap model) {
		Nursery bean = code == null ? new Nursery() : nurseryRepo.findOne(code);
		if (bean == null) bean = new Nursery();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Nursery");
		model.addAttribute("allbeans", nurseryRepo.findAll());
		model.addAttribute("types", nurseryTypeRepo.findAllByOrderBySequenceAsc());
		model.addAttribute("areas", areaRepo.findAll());
		if (bean.getId() != null) {
			List<MaterialPrice> mplist = materialPriceRepo.findAllByNursery(bean);
			model.addAttribute("materialprices", mplist);
			model.addAttribute("materials", materialRepo.findAll().stream()
					.filter(m -> mplist.stream().noneMatch(mm -> mm.getMaterial().getId().equals(m.getId())))
					.collect(Collectors.toList()));
			List<ProductPrice> pplist = productPriceRepo.findAllByNursery(bean);
			model.addAttribute("productprices", pplist);
			model.addAttribute("products", productRepo.findAll().stream()
					.filter(p -> pplist.stream().noneMatch(pp -> pp.getProduct().getId().equals(p.getId())))
					.collect(Collectors.toList()));
		}
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Nursery type) throws IOException {
		Nursery bean;
		if (type.getId() == null || (bean = nurseryRepo.findOne(type.getId())) == null) {
			Nursery lt = nurseryRepo.findTopByAreaOrderBySequenceDesc(type.getArea());
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = Nursery.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean.setAddress(type.getAddress());
		bean.setPhone(type.getPhone());
		bean.setArea(type.getArea());
		bean.setType(type.getType());
		bean.setLatitude(type.getLatitude());
		bean.setLongitude(type.getLongitude());
		bean = nurseryRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			nurseryRepo.save(updater.getData().entrySet().stream().map(e -> {
				Nursery bean = nurseryRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		nurseryRepo.delete(code);
		return true;
	}

	@RequestMapping(value="/{code}/product", method=RequestMethod.POST)
	public String addProductPrice(@PathVariable Long code, ProductPrice mprice, RedirectAttributes redirectAttrs) {
		Nursery bean = nurseryRepo.findOne(code);
		mprice.setNursery(bean);
		productPriceRepo.save(mprice);
		redirectAttrs.addFlashAttribute("clickonload", "list");
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/{code}/product/{product}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteProductPrice(@PathVariable Long code, @PathVariable Long product) {
		Nursery bean = nurseryRepo.findOne(code);
		ProductPrice pp = productPriceRepo.findOne(product);
		if (pp != null && pp.getNursery().getId().equals(bean.getId())) {
			productPriceRepo.delete(pp);
		}
		return true;
	}

	@RequestMapping(value="/{code}/material", method=RequestMethod.POST)
	public String addMaterialPrice(@PathVariable Long code, MaterialPrice mprice, RedirectAttributes redirectAttrs) {
		Nursery bean = nurseryRepo.findOne(code);
		mprice.setNursery(bean);
		materialPriceRepo.save(mprice);
		redirectAttrs.addFlashAttribute("clickonload", "list");
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/{code}/material/{material}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteMaterialPrice(@PathVariable Long code, @PathVariable Long material) {
		Nursery bean = nurseryRepo.findOne(code);
		MaterialPrice mtp = materialPriceRepo.findOne(material);
		if (mtp != null && mtp.getNursery().getId().equals(bean.getId())) {
			materialPriceRepo.delete(mtp);
		}
		return true;
	}
}


