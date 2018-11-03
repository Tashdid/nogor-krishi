package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.GardenDesignImages;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Nov 03, 2018
 *
 */
@Repository
public interface GardenDesignImagesRepo extends JpaRepository<GardenDesignImages, Long> {

	List<GardenDesignImages> findAllByOrderBySequenceAsc();
	
	GardenDesignImages findTopByOrderBySequenceDesc();

}
