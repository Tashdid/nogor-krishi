package com.niil.nogor.krishi.entity;

import javax.persistence.*;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Nov 03, 2018
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GardenDesignImages {
	private @Id @GeneratedValue Long id;
	private String details;
	private int sequence;
	private @ManyToOne ImageFile image;
}
