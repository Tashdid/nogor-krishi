package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 10, 2019
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false, unique = true)
	private String token;
	@Column(nullable = false)
	private LocalDateTime expiryDate;
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User user;
	
	public void setExpiryDate(int minutes) {
		this.expiryDate = LocalDateTime.now().plusMinutes(minutes);
	}
}
