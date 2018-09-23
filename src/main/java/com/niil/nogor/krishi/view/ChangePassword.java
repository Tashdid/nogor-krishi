package com.niil.nogor.krishi.view;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 22, 2018
 *
 */
@Data
public class ChangePassword {
	@NotEmpty(message = "দয়া করে পাসওয়ার্ড প্রদান করুন")
	private String password;
	@NotEmpty(message = "দয়া করে নতুন পাসওয়ার্ড প্রদান করুন")
	private String newPassword;
}
