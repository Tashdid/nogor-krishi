package com.niil.nogor.krishi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;

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
	private BigDecimal total_amount;
	private long delivery_charge;
	private long nursery_count;
	
	
//	private String status;
	private LocalDateTime order_time;
	private int rating;
	private String comment;
	private String phone_no;
//	private String address;
//	private String district;
//	private String city;
	
	private String billing_address;
	private String billing_district;
	private String billing_city;
	private String delivery_address;
	private String delivery_district;
	private String delivery_city;
	private String delivery_notes;
	
	private String feedbackSt;
	private LocalDateTime feedbackDate;

	@OneToMany(targetEntity=NotesOnOrder.class, mappedBy="orders", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<NotesOnOrder> notes;

	@OneToMany(targetEntity=DeliveryManagement.class, mappedBy="orders", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DeliveryManagement> deliveryManagements;
	

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
