package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

	List<Product> findAllByOrderByTypeSequenceAsc();

	Product findTopByTypeAndSequenceGreaterThanOrderBySequence(ProductType type, Integer sequence);

	Product findTopByTypeAndSequenceLessThanOrderBySequenceDesc(ProductType type, Integer sequence);

	List<Product> findByNameIgnoreCaseContaining(String name);
	
	List<Product> findAllByTypeAndParentOrderBySequenceAsc(ProductType type, Product parent);
	
	List<Product> findAllByParentOrderBySequenceAsc(Product parent);
	
	Product findTopByTypeAndParentAndSequenceGreaterThanOrderBySequence(ProductType type, Product parent, Integer sequence);

	Product findTopByTypeAndParentAndSequenceLessThanOrderBySequenceDesc(ProductType type, Product parent, Integer sequence);
	
	@Query("SELECT p FROM Product p WHERE p.id not in (SELECT pr.parent.id FROM Product pr)")
	List<Product> findAllAsChildOnly();

}
