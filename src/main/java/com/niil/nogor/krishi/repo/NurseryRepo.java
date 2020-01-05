package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Area;
import com.niil.nogor.krishi.entity.DemographicData;
import com.niil.nogor.krishi.entity.Nursery;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface NurseryRepo extends JpaRepository<Nursery, Long> {

	List<Nursery> findAllByAreaOrderBySequenceAsc(Area area);
	List<Nursery> findAllByCityOrderBySequenceAsc(DemographicData city);
	
	Nursery findTopByAreaOrderBySequenceDesc(Area area);

}
