package com.niil.nogor.krishi.controller;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niil.nogor.krishi.entity.ProductPropertyValue;
import com.niil.nogor.krishi.repo.ProductPropertyRepo;
import com.niil.nogor.krishi.repo.ProductPropertyValueRepo;

@Controller
@RequestMapping(ProductPropertyValueController.URL)
public class ProductPropertyValueController extends AbstractController{
	
	static final String URL = "/manage/productpropertyvalue";
	
	@Autowired ProductPropertyValueRepo productPropertyValueRepo;
	@Autowired ProductPropertyRepo productPropertyRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}
	
	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		ProductPropertyValue bean = code == null ? new ProductPropertyValue() : productPropertyValueRepo.findOne(code);
		if (bean == null) bean = new ProductPropertyValue();
		model.addAttribute("bean", bean);
		model.addAttribute("allbeans", productPropertyValueRepo.findAll());
		model.addAttribute("allproperties", productPropertyRepo.findAll());
		return URL.substring(1);
	}
	
	@RequestMapping(value = "/saveorupdate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Integer> saveOrUpdateProductPropertyValue(@ModelAttribute ProductPropertyValue bean) {
		try {
			if (bean != null) {
				productPropertyValueRepo.save(bean);
				return ResponseEntity.ok(HttpStatus.SC_OK);
			} else {
				return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/delete/{code}", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Integer> delete(@PathVariable Long code) {
		
		try {
			if (code != null) {
				productPropertyValueRepo.delete(code);
				return ResponseEntity.ok(HttpStatus.SC_OK);
			} else {
				return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}

	
	

}
