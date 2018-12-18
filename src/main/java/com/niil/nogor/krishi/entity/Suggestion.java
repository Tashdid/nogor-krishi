package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
public class Suggestion {
	private @Id @GeneratedValue Long id;
	@NotNull
	private @Column(nullable=false) String title;
	@NotNull
	@Column(nullable=false, length=10485760)
	private String content;
	private boolean published;
	private LocalDateTime createdOn;
	private long viewCount;
	private @ManyToOne User user;
	@OneToMany(targetEntity=Comment.class, mappedBy="suggestion", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Comment> comments;

	public Suggestion incrementViewCount() {
		viewCount = viewCount + 1;
		return this;
	}

	@PrePersist
	private void preAdd() {
		this.createdOn = LocalDateTime.now();
	}
}
