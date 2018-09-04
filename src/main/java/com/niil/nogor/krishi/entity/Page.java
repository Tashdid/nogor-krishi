package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String title;
	@Column(nullable=false, length=10485760)
	private String content;
	private boolean published;
	private int sequence;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private Menu menu;
	private long viewCount;

	public Page incrementViewCount() {
		viewCount = viewCount + 1;
		return this;
	}

	@PrePersist
	private void preAdd() {
		this.createdOn = LocalDateTime.now();
		preUpdate();
	}

	@PreUpdate
	private void preUpdate() {
		this.modifiedOn = LocalDateTime.now();
	}
}
