package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.entity.ProductPriceOnPropertyValue;
import com.niil.nogor.krishi.entity.ProductProperty;
import com.niil.nogor.krishi.entity.ProductPropertyValue;

public interface ProductPriceOnPropertyValueRepo extends JpaRepository<ProductPriceOnPropertyValue, Long>{


	public List<ProductPriceOnPropertyValue> findAllByProductPrice(ProductPrice productPrice);
}
