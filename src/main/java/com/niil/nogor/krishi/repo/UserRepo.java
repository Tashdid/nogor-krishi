package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Role;
import com.niil.nogor.krishi.entity.User;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 30, 2018
 *
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	User findByEmail(String email);
	User findByMobile(String mobile);
	List<User> findAllByNurseryIsNotNull();
	List<User> findAllByRole(Role role);
}
