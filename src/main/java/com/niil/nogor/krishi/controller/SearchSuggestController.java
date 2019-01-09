package com.niil.nogor.krishi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.niil.nogor.krishi.config.CacheRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 29, 2018
 *
 */
@RestController
public class SearchSuggestController {

	@Autowired CacheRepo cacheRepo;

	@RequestMapping("/search/suggestions")
	public List<Map<String, Object>> searchSuggestions(@RequestParam String term) {
		return cacheRepo.searchSuggestions(term);
	}

	@RequestMapping("/search/products")
	public List<Map<String, Object>> searchProducts(@RequestParam String term) {
		return cacheRepo.searchProducts(term);
	}

	@RequestMapping("/search/contents")
	public List<Map<String, Object>> searchContents(@RequestParam String term) {
		return cacheRepo.searchContents(term);
	}
}
