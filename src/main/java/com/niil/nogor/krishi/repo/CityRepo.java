package com.niil.nogor.krishi.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.City;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface CityRepo extends CrudRepository<City, Long> {

}
