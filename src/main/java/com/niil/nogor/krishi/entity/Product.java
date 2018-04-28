package com.niil.nogor.krishi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Data
@Entity
public class Product {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @Column(nullable=false) String description;
	private @Lob byte[] image;
	private String productivity;
	private String benefits;
	private int sequence;
	private @ManyToOne ProductType productType;
	private @ManyToOne SaleType saleType;
}
