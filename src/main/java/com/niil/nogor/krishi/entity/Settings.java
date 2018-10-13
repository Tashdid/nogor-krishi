package com.niil.nogor.krishi.entity;

import javax.persistence.*;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
	private @Id @GeneratedValue Long id;
	private String blogEmail;
	private String nurseryEmail;
	@Column(length=10485760)
	private String footerText;
}
