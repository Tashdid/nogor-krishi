package com.niil.nogor.krishi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.ImageFile;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
@Repository
public interface ImageFileRepo extends JpaRepository<ImageFile, Long> {

}
