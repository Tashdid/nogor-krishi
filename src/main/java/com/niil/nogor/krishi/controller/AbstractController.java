package com.niil.nogor.krishi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.niil.nogor.krishi.config.CacheRepo;
import com.niil.nogor.krishi.entity.Menu;
import com.niil.nogor.krishi.entity.Page;
import com.niil.nogor.krishi.entity.Settings;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.PageRepo;
import com.niil.nogor.krishi.repo.SettingsRepo;
import com.niil.nogor.krishi.service.SecurityService;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 5, 2018
 *
 */
public abstract class AbstractController {

	@Autowired Settings siteSettings;
	@Autowired PageRepo pageRepo;
	@Autowired SecurityService securityService;
	@Autowired SettingsRepo settingsRepo;
	@Autowired CacheRepo cacheRepo;

	@ModelAttribute("pagesMap")
	public Map<Menu, List<Page>> pagesMap() {
		Map<Menu, List<Page>> pagesMap = new HashMap<>();
		List<Page> pages = pageRepo.findAllByPublishedTrueOrderBySequenceAsc();
		pagesMap.put(Menu.PRODUCT, pagesByMenu(pages, Menu.PRODUCT));
		pagesMap.put(Menu.SERVICE, pagesByMenu(pages, Menu.SERVICE));
		pagesMap.put(Menu.SUGGESTION, pagesByMenu(pages, Menu.SUGGESTION));
		pagesMap.put(Menu.ABOUTUS, pagesByMenu(pages, Menu.ABOUTUS));
		pagesMap.put(Menu.CONTACT, pagesByMenu(pages, Menu.CONTACT));
		return pagesMap;
	}

	@ModelAttribute("luser")
	public User loggedUser() {
		return securityService.findLoggedInUser();
	}

	@ModelAttribute("siteSettings")
	public Settings siteSettings() {
		return siteSettings;
	}

	private List<Page> pagesByMenu(List<Page> pages, Enum<?> menu) {
		return pages.stream().filter(pg -> pg.getMenu() == menu).collect(Collectors.toList());
	}

	@ExceptionHandler(Exception.class)
	public String handleError() {
		return "error";
	}
}
