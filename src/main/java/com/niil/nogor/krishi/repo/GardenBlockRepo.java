package com.niil.nogor.krishi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.GardenBlock;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface GardenBlockRepo extends JpaRepository<GardenBlock, Long> {

}
