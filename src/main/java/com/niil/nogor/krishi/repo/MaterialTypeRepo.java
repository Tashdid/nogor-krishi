package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.MaterialType;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface MaterialTypeRepo extends JpaRepository<MaterialType, Long> {

	List<MaterialType> findAllByOrderBySequenceAsc();
	
	MaterialType findTopByOrderBySequenceDesc();

}
