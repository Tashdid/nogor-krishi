package com.niil.nogor.krishi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.APIOfficer;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface APIOfficerRepo extends JpaRepository<APIOfficer, Long> {

	Optional<APIOfficer> findByApiId(int apiId);
}
