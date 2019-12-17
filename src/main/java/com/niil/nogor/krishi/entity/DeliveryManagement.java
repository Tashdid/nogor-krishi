package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagement {
	private @Id @GeneratedValue Long id;
	@Column(nullable=false, length=10485760)

	private Date deliveryDate;
	@Transient
	private String deliveryDatest;
	
	private String status;
	
	private @ManyToOne DeliveryPerson deliveryPerson;

	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	@ManyToOne(targetEntity=Orders.class)
	private Orders orders;
	@OneToOne(targetEntity=Nursery.class)
	private Nursery nursery;

	@PrePersist
	private void preAdd() {
		this.createdAt = LocalDateTime.now();
	}
}
