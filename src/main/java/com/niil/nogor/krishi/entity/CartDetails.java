package com.niil.nogor.krishi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

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
	private String sessionId;
//	private @OneToOne(targetEntity=Product.class) Product product;
//	private @OneToOne(targetEntity=SaleType.class) SaleType saleType;
	private BigDecimal unit_price;
	private int quantity;
	private LocalDateTime createdOn;
//	private @ManyToOne(targetEntity=Nursery.class) Nursery nursery;
	private @ManyToOne(targetEntity = ProductPrice.class) ProductPrice productPrice;
	

	@PrePersist
	private void preAdd() {
		this.createdOn = LocalDateTime.now();
	}

}
