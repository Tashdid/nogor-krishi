package com.niil.nogor.krishi.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailsDTO {
	private Long cart_id;
//	private Long product_id;
//	private Long sale_type_id;
	private BigDecimal unit_price;
	private int quantity;
//	private Long nursery_id;
	private Long productPrice_id;
	

}
