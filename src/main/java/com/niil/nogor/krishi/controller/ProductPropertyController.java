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

import com.niil.nogor.krishi.entity.ProductProperty;
import com.niil.nogor.krishi.repo.ProductPropertyRepo;

@Controller
@RequestMapping(ProductPropertyController.URL)
public class ProductPropertyController extends AbstractController{
	
	static final String URL = "/manage/productproperty";
	
	@Autowired ProductPropertyRepo productPropertyRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}
	
	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, ModelMap model) {
		ProductProperty bean = code == null ? new ProductProperty() : productPropertyRepo.findOne(code);
		if (bean == null) bean = new ProductProperty();
		model.addAttribute("bean", bean);
		model.addAttribute("allbeans", productPropertyRepo.findAll());
		return URL.substring(1);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute ProductProperty bean) {
		try {
			if (bean != null) {
				ProductProperty newBean=productPropertyRepo.save(bean);
				return ResponseEntity.ok(HttpStatus.SC_OK);
		} else {
			return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	} catch (Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
	}
	
	@RequestMapping(value="/delete/{code}")
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		
		try {
			if (code != null) {
				productPropertyRepo.delete(code);
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	
	

}