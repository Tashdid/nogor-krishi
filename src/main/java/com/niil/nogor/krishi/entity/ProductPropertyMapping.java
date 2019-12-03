package com.niil.nogor.krishi.entity;

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
public class ProductPropertyMapping {
	private @Id @GeneratedValue Long id;
	private @OneToOne Product product;
	private @OneToOne ProductProperty productProperty;
}
