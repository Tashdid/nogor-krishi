package com.niil.nogor.krishi.repo;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.niil.nogor.krishi.entity.GardenLayout;
import com.niil.nogor.krishi.entity.SaleType;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class GardenLayoutRepoTest {

	@Autowired
	GardenLayoutRepo gardenLayoutRepo;
	@Autowired
	SaleTypeRepo saleTypeRepo;

	@Test
	public void testGardenLayoutAdd() {
		((ArrayList<SaleType>) saleTypeRepo.findAll()).stream().forEach(System.out::println);;
		GardenLayout lo = new GardenLayout();
		lo.setLength(20);
		lo.setWidth(15);

		gardenLayoutRepo.save(lo);
		
		((ArrayList<GardenLayout>) gardenLayoutRepo.findAll()).stream().forEach(System.out::println);;
	}

}
