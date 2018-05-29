package com.niil.nogor.krishi.entity;

import java.math.BigDecimal;

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
public class MaterialPrice {
	private @Id @GeneratedValue Long id;
	private BigDecimal price;
	private @ManyToOne Material material;
	private @ManyToOne Nursery nursery;
}
