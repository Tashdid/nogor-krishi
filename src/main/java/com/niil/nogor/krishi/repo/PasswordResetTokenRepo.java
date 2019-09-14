package com.niil.nogor.krishi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.PasswordResetToken;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 10, 2019
 *
 */
@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);
}
