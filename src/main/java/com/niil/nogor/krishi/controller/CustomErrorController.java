package com.niil.nogor.krishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.config.AppConfig;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 20, 2018
 *
 */
@Controller
public class CustomErrorController extends AbstractController implements ErrorController {
	@Autowired AppConfig appConfig;

	@RequestMapping("/error")
	public String errorPage() {
		return "error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
