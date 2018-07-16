package com.niil.nogor.krishi.view;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jul 17, 2018
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
	private String username;
	private String name;
	private String email;
	private String role;
}
