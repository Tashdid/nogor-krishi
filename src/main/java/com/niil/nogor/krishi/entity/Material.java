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
public class Material {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @ManyToOne MaterialType type;
	private @OneToMany(mappedBy="material", cascade=CascadeType.ALL, orphanRemoval=true) List<MaterialPrice> prices;
}
