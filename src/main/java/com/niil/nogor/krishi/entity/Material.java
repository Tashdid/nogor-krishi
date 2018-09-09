package com.niil.nogor.krishi.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Material {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @ManyToOne MaterialType type;
}
