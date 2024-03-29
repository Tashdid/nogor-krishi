package com.niil.nogor.krishi.dto;

import java.math.BigDecimal;

import javax.persistence.Transient;

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
	private String billing_address;
	private String billing_district;
	private String billing_city;
	private String delivery_address;
	private String delivery_district;
	private String delivery_city;
	private String delivery_notes;
	
	private boolean new_delivery_address;
	
	private boolean new_billing_address;
//	private BigDecimal total_price;
	private String order_status;

}
