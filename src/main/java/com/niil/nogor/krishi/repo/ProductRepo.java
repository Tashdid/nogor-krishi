package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductType;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	List<Product> findAllByTypeOrderBySequenceAsc(ProductType type);

	Product findTopByTypeOrderBySequenceDesc(ProductType type);

}
