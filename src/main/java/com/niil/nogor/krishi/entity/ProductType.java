package com.niil.nogor.krishi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;
import lombok.ToString;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Data
@Entity
@ToString(exclude={"image", "icon"})
public class ProductType {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @Lob byte[] image;
	private @Lob byte[] icon;
	private @Column(unique=true) int sequence;
}
