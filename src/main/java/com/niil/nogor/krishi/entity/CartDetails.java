package com.niil.nogor.krishi.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDetails {
	private @Id @GeneratedValue Long id;
	private @ManyToOne(targetEntity=Cart.class) Cart cart;
	private @OneToOne(targetEntity=Product.class) Product product;
	private @OneToOne(targetEntity=SaleType.class) SaleType saleType;
	private BigDecimal unit_price;
	private int quantity;
	private @ManyToOne(targetEntity=Nursery.class) Nursery nursery;

}
