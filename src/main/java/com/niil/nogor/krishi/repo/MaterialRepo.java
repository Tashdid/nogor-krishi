package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Material;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface MaterialRepo extends JpaRepository<Material, Long> {

	List<Material> findAllByOrderBySequenceAsc();

	Material findTopByOrderBySequenceDesc();

	Material findTopBySequenceGreaterThanOrderBySequence(Integer sequence);

	Material findTopBySequenceLessThanOrderBySequenceDesc(Integer sequence);
}
