package com.niil.nogor.krishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.config.AppConfig;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since May 31, 2018
 *
 */
@Controller
@RequestMapping("/manage")
public class AdminController extends AbstractController {
	@Autowired AppConfig appConfig;

	@RequestMapping
	public String manageHome() {
		return "redirect:/manage/product";
	}
}
