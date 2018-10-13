package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.CarouselImages;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
@Repository
public interface CarouselImagesRepo extends JpaRepository<CarouselImages, Long> {

	List<CarouselImages> findAllByOrderBySequenceAsc();
	
	CarouselImages findTopByOrderBySequenceDesc();

}
