package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Area;
import com.niil.nogor.krishi.entity.City;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface AreaRepo extends JpaRepository<Area, Long> {

	List<Area> findAllByOrderBySequenceAsc();

	List<Area> findAllByCityOrderBySequenceAsc(City city);
	
	Area findTopByCityOrderBySequenceDesc(City city);

}
