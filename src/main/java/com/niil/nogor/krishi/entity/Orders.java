package com.niil.nogor.krishi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Orders implements Comparable<Orders>{
	private @Id @GeneratedValue Long id;
	private  Long orders_id;
	private @OneToOne(targetEntity=User.class) User user;
	private BigDecimal payable_amount;
	private String status;
	private LocalDateTime order_time;
	private int rating;
	private String comment;
	private String phone_no;
	private String address;
	
	@Override
	public int compareTo(Orders o) {
		// TODO Auto-generated method stub
		if (o.getId() > this.getId()) {
	          return 1;
	        } else {
	          return 0;
	        }
	}
}
