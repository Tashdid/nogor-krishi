package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductPropertyMapping;

public interface ProductPropertyMappingRepo extends JpaRepository<ProductPropertyMapping, Long>{

	public List<ProductPropertyMapping> findAllByProduct(Product product);
	
}
