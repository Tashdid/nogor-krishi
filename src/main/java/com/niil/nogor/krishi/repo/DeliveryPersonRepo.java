package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.DeliveryPerson;


@Repository
public interface DeliveryPersonRepo extends JpaRepository<DeliveryPerson, Long> {

	List<DeliveryPerson> findAllByOrderBySequenceAsc();
	
	DeliveryPerson findTopByOrderBySequenceDesc();

}
