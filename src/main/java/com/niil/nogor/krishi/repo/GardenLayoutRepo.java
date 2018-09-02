package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.GardenLayout;
import com.niil.nogor.krishi.entity.User;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface GardenLayoutRepo extends JpaRepository<GardenLayout, Long> {
	List<GardenLayout> findByUserOrderByCreatedOnDesc(User user);
}
