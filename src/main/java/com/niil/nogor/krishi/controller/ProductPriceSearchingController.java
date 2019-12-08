package com.niil.nogor.krishi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.entity.ProductPriceOnPropertyValue;
import com.niil.nogor.krishi.entity.ProductProperty;
import com.niil.nogor.krishi.entity.ProductPropertyMapping;
import com.niil.nogor.krishi.entity.ProductPropertyValue;
import com.niil.nogor.krishi.entity.SaleType;
import com.niil.nogor.krishi.repo.ProductPriceOnPropertyValueRepo;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.repo.ProductPropertyMappingRepo;
import com.niil.nogor.krishi.repo.ProductPropertyValueRepo;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.SaleTypeRepo;
import com.niil.nogor.krishi.repo.UserRepo;

@RestController
@RequestMapping(ProductPriceSearchingController.URL)
public class ProductPriceSearchingController extends AbstractController {
	static final String URL = "/api/productpricesearch"; // /test/price-list

	@Autowired
	ProductPriceRepo priceRepo;
	@Autowired
	ProductRepo productRepo;
	@Autowired
	ProductPropertyValueRepo productPropertyValueRepo;
	@Autowired
	ProductPriceOnPropertyValueRepo productPriceOnPropertyValueRepo;
	@Autowired
	ProductPropertyMappingRepo productPropertyMappingRepo;
	@Autowired
	SaleTypeRepo saleTypeRepo;
	@Autowired
	UserRepo userRepo;

	@RequestMapping(value = "/{productId}/saleType/{saleTypeId}", method = RequestMethod.GET)
	public List<ProductPrice> getProductPrices(@PathVariable Long productId, @PathVariable Long saleTypeId) {

		System.out.print("Session attribute is " + RequestContextHolder.currentRequestAttributes().getSessionId());

		Product product = productRepo.findOne(productId);
		SaleType saleType = saleTypeRepo.findOne(saleTypeId);
		return priceRepo.findAllByProductAndSaleType(product, saleType);

	}

	
	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public List<ProductPrice> getProductPricesByProduct(@PathVariable Long productId) {

		System.out.println("============================================================================");
		return getProductPricesBySearch(productId, null);

	}
	

	@RequestMapping(value = "/{productId}/{valueSt}", method = RequestMethod.GET)
	public List<ProductPrice> getProductPricesBySearch(@PathVariable Long productId,@PathVariable String valueSt) {

		System.out.println("============================================================================");
		List<ProductPrice> priceList=new ArrayList<>();

		List<String> idList=null;
		if(valueSt!=null) {
			idList=Arrays.asList(valueSt.split("-"));
		}
		Product product = productRepo.findOne(productId);
		if(idList==null) {
			priceList=priceRepo.findAllByProductOrderByQuantityDescPriceAsc(product);
		}else {
			priceList=priceRepo.getProductPriceListByPropertyValueOrderByQuantityDescPriceAsc(productId,idList);
		}
		priceList.forEach(price->{
			price.getProductPriceOnPropertyValueList().forEach(pp->{
				pp.setProductPrice(null);
			});
		});
		return priceList;

	}

	
}
