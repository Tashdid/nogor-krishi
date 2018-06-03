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
	private @Column(nullable=false) Double top;
	private @Column(nullable=false) Double left;
	private @Column(nullable=false) Double height;
	private @Column(nullable=false) Double width;
	private @ManyToOne Product product;
	private @ManyToOne GardenLayout gardenLayout;
}
