package com.niil.nogor.krishi.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class ProductPrice {
	private @Id @GeneratedValue Long id;
	private BigDecimal price;
	private @ManyToOne(targetEntity=Product.class) Product product;
	private @ManyToOne(targetEntity=SaleType.class) SaleType saleType;
	private @ManyToOne(targetEntity=Nursery.class) Nursery nursery;
	private Long quantity;
	
	@JsonIgnore
	@OneToMany(targetEntity=ProductPriceOnPropertyValue.class, mappedBy="productPrice", cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	private List<ProductPriceOnPropertyValue> productPriceOnPropertyValueList;

	
	
}
