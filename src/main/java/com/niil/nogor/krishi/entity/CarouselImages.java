package com.niil.nogor.krishi.entity;

import javax.persistence.*;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarouselImages {
	private @Id @GeneratedValue Long id;
	private int sequence;
	private @ManyToOne ImageFile image;
}
