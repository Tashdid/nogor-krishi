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
public class City {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @Column(unique=true) int sequence;
}
