package com.niil.nogor.krishi.entity;

import javax.persistence.*;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 29, 2018
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue 
	private Long id;

	@Column(nullable=false)
	@NotEmpty(message = "দয়া করে নাম প্রদান করুন")
	private String name;

	@Column(nullable=false)
	@NotEmpty(message = "দয়া করে পাসওয়ার্ড প্রদান করুন")
	private String password;

	@Column(nullable = false, unique = true)
	@NotEmpty(message = "দয়া করে মোবাইল নং প্রদান করুন")
	private String mobile;

	@Email(message = "দয়া করে সঠিক মেইল প্রদান করুন")
	private String email;

	private boolean disabled;
	private String idpref;
	private Role role;
}
