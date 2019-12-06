package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niil.nogor.krishi.entity.ProductProperty;
import com.niil.nogor.krishi.entity.ProductPropertyValue;

public interface ProductPropertyValueRepo extends JpaRepository<ProductPropertyValue, Long>{
	
	public List<ProductPropertyValue> findAllByProductProperty(ProductProperty productProperty);

}
