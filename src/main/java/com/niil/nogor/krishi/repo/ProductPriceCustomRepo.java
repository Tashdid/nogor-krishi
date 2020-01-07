package com.niil.nogor.krishi.repo;

import java.util.List;

import com.niil.nogor.krishi.entity.ProductPrice;

public interface ProductPriceCustomRepo {
	List<ProductPrice> getProductPriceListByPropertyValue(Long productId,List<String> propertyValueIdList);
	
	List<ProductPrice> getProductPriceListByPropertyValueOrderByQuantityDescPriceAsc(Long productId,List<String> propertyValueIdList);
	
	List<ProductPrice> getProductPriceListByPropertyValueAndDemographicDataOrderByQuantityDescPriceAsc(Long productId,List<String> propertyValueIdList, String demographicData) ;
	
	List<ProductPrice> getProductPriceListByProductAndDemographicDataOrderByQuantityDescPriceAsc(Long productId, String demographicData) ;
	
}
