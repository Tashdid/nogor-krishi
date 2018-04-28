package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.ProductType;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface ProductTypeRepo extends CrudRepository<ProductType, Long> {

	List<ProductType> findAllByName(@Param("name") String name);
}
