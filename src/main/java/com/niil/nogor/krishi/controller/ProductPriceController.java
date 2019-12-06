package com.niil.nogor.krishi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.entity.ProductPriceOnPropertyValue;
import com.niil.nogor.krishi.entity.ProductProperty;
import com.niil.nogor.krishi.entity.ProductPropertyMapping;
import com.niil.nogor.krishi.entity.ProductPropertyValue;
import com.niil.nogor.krishi.entity.SaleType;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.repo.ProductPropertyMappingRepo;
import com.niil.nogor.krishi.repo.ProductPropertyValueRepo;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.SaleTypeRepo;

@Controller
@RequestMapping(ProductPriceController.URL)
public class ProductPriceController extends AbstractController{
	static final String URL = "/nursery/productprice"; // /test/price-list
	
	@Autowired ProductPriceRepo priceRepo;
	@Autowired ProductRepo productRepo;
	@Autowired ProductPropertyValueRepo productPropertyValueRepo;
	@Autowired ProductPropertyMappingRepo productPropertyMappingRepo;
	@Autowired SaleTypeRepo saleTypeRepo;
	
	@RequestMapping(value="/{productId}/saleType/{saleTypeId}",method=RequestMethod.GET)
	@ResponseBody
	public List<ProductPrice> getProductPrices(@PathVariable Long productId,@PathVariable Long saleTypeId) {
		
		System.out.print("Session attribute is "+ RequestContextHolder.currentRequestAttributes().getSessionId());
		
		Product product = productRepo.findOne(productId);
		SaleType saleType = saleTypeRepo.findOne(saleTypeId);
		return priceRepo.findAllByProductAndSaleType(product, saleType);

	}
	
	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null,null, model);
	}
	

	@RequestMapping(value = "/{productId}")
	public String updateProductScreen(@PathVariable Long productId, ModelMap model) {
		return updateScreen(productId,null, model);
	}
	
	@RequestMapping(value = "/{productId}/{code}")
	public String updateScreen(@PathVariable Long productId, @PathVariable Long code, ModelMap model) {
		List<Product> productList = productRepo.findAll();
		
		Product product = new Product();
		if (productId != null) {
			product = productRepo.findOne(productId);
		} else {
			if (!productList.isEmpty())
				product = productList.get(0);
		}
		ProductPrice bean = code == null || priceRepo.findOne(code)==null ? new ProductPrice() : priceRepo.findOne(code);
		
		
		List<ProductPropertyMapping> propertyMappingList = productPropertyMappingRepo.findAllByProduct(product);
		Map<Long, List<ProductPropertyValue>> mapProperty=new HashMap<Long, List<ProductPropertyValue>>();
		propertyMappingList.forEach(productPropertyMapping->{
			 List<ProductPropertyValue> propertyValueList=productPropertyValueRepo.findAllByProductProperty(productPropertyMapping.getProductProperty());
			if(bean.getId()==null) {
				ProductPriceOnPropertyValue productPriceOnPropertyValue=new ProductPriceOnPropertyValue();
				productPriceOnPropertyValue.setProductPropertyValue(propertyValueList!=null &&!propertyValueList.isEmpty()?propertyValueList.get(0):null);
				if(bean.getProductPriceOnPropertyValueList()==null) {
					bean.setProductPriceOnPropertyValueList(new ArrayList<>());
				}
				bean.getProductPriceOnPropertyValueList().add(productPriceOnPropertyValue);
			}
			mapProperty.put(productPropertyMapping.getProductProperty().getId(), propertyValueList);
		});
		
		
		//List<ProductProperty> propertyList = propertyMappingList.stream().forEach(p->p.getProductProperty()).collect(Collectors.toList());
		//List<ProductPropertyValue> propertyValueList = propertyList.stream().forEach(p->productPropertyValueRepo.findAllByProductProperty(p));
		
		
		bean.setProduct(product);
		model.addAttribute("bean", bean);
		model.addAttribute("allbeans", priceRepo.findAll());
		model.addAttribute("allproducts", productList);
		model.addAttribute("mapProperty", mapProperty);
		
		

		return URL.substring(1);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@Valid ProductPrice bean) {
		
		bean.getProductPriceOnPropertyValueList().forEach(productPrice->{
			productPrice.setProductPrice(bean);
		});

		priceRepo.save(bean);
		
		return "redirect:" + URL + "/" + bean.getProduct().getId()+"/"+bean.getId();
	}
	
	
}
