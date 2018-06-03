package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

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
public class GardenLayout {
	@Id
	@GenericGenerator(name = "seq", strategy = "com.niil.nogor.krishi.entity.LayoutIdGenerator")
	@GeneratedValue(generator="seq")
	private Long id;
	private @Column(nullable=false) Integer length;
	private @Column(nullable=false) Integer width;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

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
