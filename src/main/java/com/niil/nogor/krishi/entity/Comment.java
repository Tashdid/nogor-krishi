package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 16, 2018
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	private @Id @GeneratedValue Long id;
	@Column(nullable=false, length=10485760)
	private String comment;
	private boolean published;
	private LocalDateTime createdOn;
	private @ManyToOne User user;
	@ManyToOne(targetEntity=Suggestion.class)
	private Suggestion suggestion;

	@PrePersist
	private void preAdd() {
		this.createdOn = LocalDateTime.now();
	}
}
