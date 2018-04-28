package com.niil.nogor.krishi.repo;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.niil.nogor.krishi.entity.ProductType;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductTypeRepoTest {

	@Autowired
	ProductTypeRepo productTypeRepo;

	@Test
	public void testProductTypeAdd() {
		ProductType productType = new ProductType();
		productType.setName("shak");
		productType.setSequence(1);
		
		productTypeRepo.save(productType);
		
		((ArrayList<ProductType>) productTypeRepo.findAll()).stream().forEach(System.out::println);;
	}

}
