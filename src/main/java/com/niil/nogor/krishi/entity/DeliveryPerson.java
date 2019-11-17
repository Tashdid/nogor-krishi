package com.niil.nogor.krishi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPerson {

	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private String phoneNumber;
	private int sequence;

}
