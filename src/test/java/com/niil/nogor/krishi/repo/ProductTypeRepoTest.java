package com.niil.nogor.krishi.repo;

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
		productType.setAlternativeName("Shak");
		ProductType lastEntry = productTypeRepo.findTopByOrderBySequenceDesc();
		productType.setSequence((lastEntry == null ? 0 : lastEntry.getSequence()) + 1);
		
		productTypeRepo.save(productType);
		
		productTypeRepo.findAll().stream().forEach(System.out::println);;
	}

}
