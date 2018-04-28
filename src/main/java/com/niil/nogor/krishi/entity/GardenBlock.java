package com.niil.nogor.krishi.entity;

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
public class GardenBlock {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) Integer startLength;
	private @Column(nullable=false) Integer startWidth;
	private @Column(nullable=false) Integer endLength;
	private @Column(nullable=false) Integer endWidth;
	private @ManyToOne Product product;
	private @ManyToOne GardenLayout gardenLayout;
}
