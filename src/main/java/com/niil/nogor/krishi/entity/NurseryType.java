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
@ToString(exclude={"image"})
public class NurseryType {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @Lob byte[] image;
	private @Column(unique=true) int sequence;
}
