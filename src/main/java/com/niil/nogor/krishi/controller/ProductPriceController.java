package com.niil.nogor.krishi.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.entity.SaleType;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.SaleTypeRepo;

@RestController

@RequestMapping(ProductPriceController.URL)
public class ProductPriceController {
	static final String URL = "/test/price-list";
	
	@Autowired ProductPriceRepo priceRepo;
	@Autowired ProductRepo productRepo;
	@Autowired SaleTypeRepo saleTypeRepo;
	
	@RequestMapping(value="/{productId}/saleType/{saleTypeId}",method=RequestMethod.GET)
	public List<ProductPrice> getProductPrices(@PathVariable Long productId,@PathVariable Long saleTypeId) {
		
		System.out.print("Session attribute is "+ RequestContextHolder.currentRequestAttributes().getSessionId());
		
		Product product = productRepo.findOne(productId);
		SaleType saleType = saleTypeRepo.findOne(saleTypeId);
		return priceRepo.findAllByProductAndSaleType(product, saleType);

	}
}
