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
public class OrderForm {
	private String phoneNo;
	private String address;
	private BigDecimal total_price;
	private String order_status;

}
