package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.GalleryImages;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
@Repository
public interface GalleryImagesRepo extends JpaRepository<GalleryImages, Long> {

	List<GalleryImages> findAllByOrderBySequenceAsc();
	
	GalleryImages findTopByOrderBySequenceDesc();

}
