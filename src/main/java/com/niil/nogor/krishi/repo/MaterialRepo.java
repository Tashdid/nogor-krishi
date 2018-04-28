package com.niil.nogor.krishi.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Material;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface MaterialRepo extends CrudRepository<Material, Long> {

}
