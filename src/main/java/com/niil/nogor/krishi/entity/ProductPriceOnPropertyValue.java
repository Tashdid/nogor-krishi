package com.niil.nogor.krishi.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class ProductPriceOnPropertyValue {
	private @Id @GeneratedValue Long id;
	private @ManyToOne(targetEntity = ProductPrice.class, fetch = FetchType.LAZY) ProductPrice productPrice;
	private @OneToOne ProductPropertyValue productPropertyValue;
}
