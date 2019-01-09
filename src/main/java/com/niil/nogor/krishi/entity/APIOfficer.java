package com.niil.nogor.krishi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jan 5, 2019
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIOfficer {
	private @Id @GeneratedValue Long id;
	protected int apiId;
	private String name;
	private String email;
	private String role;
	private String address;
	private String mobile;
	private String avatar;
}
