package com.niil.nogor.krishi.repo;

import java.util.List;

import com.niil.nogor.krishi.entity.ProductPrice;

public interface ProductPriceCustomRepo {
	List<ProductPrice> getProductPriceListByPropertyValue(Long productId,List<String> propertyValueIdList);
}
