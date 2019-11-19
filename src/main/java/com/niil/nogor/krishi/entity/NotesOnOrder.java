package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotesOnOrder {
	private @Id @GeneratedValue Long id;
	@Column(nullable=false, length=10485760)
	private String notesSt;
	private boolean published;
	private LocalDateTime createdOn;
	private @ManyToOne User user;
	@ManyToOne(targetEntity=Orders.class)
	private Orders orders;

	@PrePersist
	private void preAdd() {
		this.createdOn = LocalDateTime.now();
	}
}
