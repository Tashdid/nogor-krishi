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
	private @Column(nullable=false) Double ptop;
	private @Column(nullable=false) Double pleft;
	private @Column(nullable=false) Double cheight;
	private @Column(nullable=false) Double cwidth;
	private @ManyToOne Product product;
	@ManyToOne(targetEntity=GardenLayout.class)
	private GardenLayout gardenLayout;
}
