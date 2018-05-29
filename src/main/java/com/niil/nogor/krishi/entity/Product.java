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
@ToString(exclude={"image", "icon"})
public class Product {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @Column(nullable=false, length=10485760) String description;
	private String alternativeName;
	private @Lob byte[] image;
	private @Lob byte[] icon;
	private String productivity;
	@Column(length=10485760)
	private String benefits;
	private int sequence;
	private @ManyToOne ProductType type;
	private @ManyToOne SaleType saleType;
	private @ManyToMany(targetEntity=Material.class) List<Material> materials;
}
