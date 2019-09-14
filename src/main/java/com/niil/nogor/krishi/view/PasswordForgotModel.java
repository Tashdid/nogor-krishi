package com.niil.nogor.krishi.view;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 10, 2019
 *
 */
public @Data class PasswordForgotModel {

	@Email
	@NotEmpty
	private String email;
}
