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
@ToString(exclude={"image", "icon"})
public class ProductType {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private String alternativeName;
	private @Lob byte[] image;
	private @Lob byte[] icon;
	private int sequence;
	private boolean linkedToMaterial;
}
