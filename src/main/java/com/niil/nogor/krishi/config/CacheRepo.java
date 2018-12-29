package com.niil.nogor.krishi.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductType;
import com.niil.nogor.krishi.entity.Suggestion;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.SuggestionRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 29, 2018
 *
 */
@Component
public class CacheRepo {
	private static final String ALL_SUGGESTIONS_CACHE = "all-suggestions";
	private static final String SUGGESTIONS_SEARCH_CACHE = "suggestions-search";
	private static final String ALL_PRODUCTS_CACHE = "all-products";
	private static final String PRODUCTS_CACHE = "products";
	private static final String PRODUCTS_SEARCH_CACHE = "products-search";

	@Autowired SuggestionRepo suggestionRepo;
	@Autowired ProductRepo productRepo;

	@Cacheable(value = ALL_SUGGESTIONS_CACHE)
	public List<Suggestion> getAllSuggestions() {
		return suggestionRepo.findAllByPublishedTrueOrderByCreatedOnDesc();
	}

	@Cacheable(value = SUGGESTIONS_SEARCH_CACHE)
	public List<Map<String, Object>> searchSuggestions(String term) {
		return suggestionRepo.findByPublishedTrueAndTitleIgnoreCaseContaining(term)
					.stream().map(s -> {
						Map<String, Object> lst = new HashMap<>();
						lst.put("key", s.getId());
						lst.put("value", s.getTitle());
						return lst;
					}).collect(Collectors.toList());
	}

	@CacheEvict(value = {ALL_SUGGESTIONS_CACHE, SUGGESTIONS_SEARCH_CACHE}, allEntries=true)
	public void removeAllSuggestions() {/** Nothing to do here */}

	@Cacheable(value = ALL_PRODUCTS_CACHE)
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	@Cacheable(value = PRODUCTS_CACHE, key = "#type.id")
	public List<Product> getProducts(ProductType type) {
		return productRepo.findAllByTypeOrderBySequenceAsc(type);
	}

	@Cacheable(value = PRODUCTS_SEARCH_CACHE)
	public List<Map<String, Object>> searchProducts(String term) {
		return productRepo.findByNameIgnoreCaseContaining(term)
					.stream().map(s -> {
						Map<String, Object> lst = new HashMap<>();
						lst.put("key", s.getId());
						lst.put("value", String.format("%s - %s", s.getName(), s.getType().getName()));
						lst.put("data", s.getType().getId());
						return lst;
					}).collect(Collectors.toList());
	}

	@CacheEvict(value = {ALL_PRODUCTS_CACHE, PRODUCTS_CACHE, PRODUCTS_SEARCH_CACHE}, allEntries=true)
	public void removeAllProductsCache() {/** Nothing to do here */}
}
