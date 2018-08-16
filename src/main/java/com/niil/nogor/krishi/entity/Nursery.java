package com.niil.nogor.krishi.entity;

import java.util.List;

import javax.persistence.*;

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
public class Nursery {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private String address;
	private String phone;
	private String password;
	private Double latitude;
	private Double longitude;
	private int sequence;
	private @ManyToOne NurseryType type;
	private @ManyToOne Area area;
	private @OneToMany(mappedBy="nursery", cascade=CascadeType.ALL, orphanRemoval=true) List<ProductPrice> productPrices;
	private @OneToMany(mappedBy="nursery", cascade=CascadeType.ALL, orphanRemoval=true) List<MaterialPrice> materialPrices;
}
