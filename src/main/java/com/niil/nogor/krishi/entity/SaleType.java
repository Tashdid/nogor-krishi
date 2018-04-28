package com.niil.nogor.krishi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Data
@Entity
public class SaleType {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
}
