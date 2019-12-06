package com.niil.nogor.krishi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.niil.nogor.krishi.repo.ProductPriceOnPropertyValueRepo;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.repo.ProductPropertyMappingRepo;
import com.niil.nogor.krishi.repo.ProductPropertyValueRepo;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.SaleTypeRepo;
import com.niil.nogor.krishi.repo.UserRepo;

@Controller
@RequestMapping(ProductPriceController.URL)
public class ProductPriceController extends AbstractController {
	static final String URL = "/nursery/productprice"; // /test/price-list

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
	@ResponseBody
	public List<ProductPrice> getProductPrices(@PathVariable Long productId, @PathVariable Long saleTypeId) {

		System.out.print("Session attribute is " + RequestContextHolder.currentRequestAttributes().getSessionId());

		Product product = productRepo.findOne(productId);
		SaleType saleType = saleTypeRepo.findOne(saleTypeId);
		return priceRepo.findAllByProductAndSaleType(product, saleType);

	}

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, null, model);
	}

	@RequestMapping(value = "/{productId}")
	public String updateProductScreen(@PathVariable Long productId, ModelMap model) {
		return updateScreen(productId, null, model);
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
		ProductPrice bean = code == null || priceRepo.findOne(code) == null ? new ProductPrice()
				: priceRepo.findOne(code);

		List<ProductPropertyMapping> propertyMappingList = productPropertyMappingRepo.findAllByProduct(product);
		Map<Long, List<ProductPropertyValue>> mapProperty = new HashMap<Long, List<ProductPropertyValue>>();
		propertyMappingList.forEach(productPropertyMapping -> {
			List<ProductPropertyValue> propertyValueList = productPropertyValueRepo
					.findAllByProductProperty(productPropertyMapping.getProductProperty());

			mapProperty.put(productPropertyMapping.getProductProperty().getId(), propertyValueList);

			if (bean.getId() == null) {

				ProductPriceOnPropertyValue productPriceOnPropertyValue = new ProductPriceOnPropertyValue();
				ProductPropertyValue newProductPropertyValue = new ProductPropertyValue();
				newProductPropertyValue.setProductProperty(productPropertyMapping.getProductProperty());

				productPriceOnPropertyValue.setProductPropertyValue(newProductPropertyValue);
				if (bean.getProductPriceOnPropertyValueList() == null) {
					bean.setProductPriceOnPropertyValueList(new ArrayList<>());
				}
				bean.getProductPriceOnPropertyValueList().add(productPriceOnPropertyValue);

			} else {

				// Set<ProductProperty> allPropertiesFromAProduct =
				// propertyMappingList.stream().map(ProductPropertyMapping::getProductProperty).collect(Collectors.toSet());
				Set<ProductProperty> allPropertiesFromBean = bean.getProductPriceOnPropertyValueList().stream()
						.map(ProductPriceOnPropertyValue::getProductPropertyValue)
						.map(ProductPropertyValue::getProductProperty).collect(Collectors.toSet());
				if (!allPropertiesFromBean.contains(productPropertyMapping.getProductProperty())
						&& propertyValueList != null && !propertyValueList.isEmpty()) {
					ProductPriceOnPropertyValue productPriceOnPropertyValue = new ProductPriceOnPropertyValue();
					ProductPropertyValue newProductPropertyValue = new ProductPropertyValue();
					newProductPropertyValue.setProductProperty(productPropertyMapping.getProductProperty());
					productPriceOnPropertyValue.setProductPropertyValue(newProductPropertyValue);

					if (bean.getProductPriceOnPropertyValueList() == null) {
						bean.setProductPriceOnPropertyValueList(new ArrayList<>());
					}
					bean.getProductPriceOnPropertyValueList().add(productPriceOnPropertyValue);
				}
			}

		});

		// List<ProductProperty> propertyList =
		// propertyMappingList.stream().forEach(p->p.getProductProperty()).collect(Collectors.toList());
		// List<ProductPropertyValue> propertyValueList =
		// propertyList.stream().forEach(p->productPropertyValueRepo.findAllByProductProperty(p));

		bean.setProduct(product);
		model.addAttribute("bean", bean);
		List<ProductPrice> priceList = priceRepo.findAll();
		model.addAttribute("allbeans", priceList);
		model.addAttribute("allproducts", productList);
		model.addAttribute("mapProperty", mapProperty);

		return URL.substring(1);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@Valid ProductPrice bean) {
		bean.setNursery(
				userRepo.findByMobile(SecurityContextHolder.getContext().getAuthentication().getName()).getNursery());

		List<ProductPriceOnPropertyValue> productPropertyValueMapList = bean.getProductPriceOnPropertyValueList();
		bean.setProductPriceOnPropertyValueList(new ArrayList<>());
		priceRepo.save(bean);

		if (productPropertyValueMapList != null && !productPropertyValueMapList.isEmpty()) {
			productPropertyValueMapList.forEach(productPropertyValueMap -> {

				if (productPropertyValueMap.getProductPropertyValue() == null
						|| productPropertyValueMap.getProductPropertyValue().getId().equals(0)) {

				} else {
					productPropertyValueMap.setProductPrice(bean);
					bean.getProductPriceOnPropertyValueList().add(productPropertyValueMap);
					productPriceOnPropertyValueRepo.save(productPropertyValueMap);
				}

			});
		}

		return "redirect:" + URL + "/" + bean.getProduct().getId() + "/" + bean.getId();
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		priceRepo.delete(code);
		return true;
	}

}
