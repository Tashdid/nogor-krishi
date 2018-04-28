package com.niil.nogor.krishi.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.ProductPrice;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface ProductPriceRepo extends CrudRepository<ProductPrice, Long> {

}
