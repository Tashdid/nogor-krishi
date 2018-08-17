package com.niil.nogor.krishi.entity;

import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

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
	private String alternativeName;
	private @Column(length=10485760) String description;
	private @Column(length=10485760) String usefulVarieties;
	private @Column(length=10485760) String suitableContainer;
	private @Column(length=10485760) String suitableTime;
	private @Column(length=10485760) String landPreparation;
	private @Column(length=10485760) String seedlingType;
	private @Column(length=10485760) String careness;
	private @Column(length=10485760) String place;
	private @Column(length=10485760) String hormone;
	private @Column(length=10485760) String pesticides;
	private @Column(length=10485760) String caution;
	private @Lob byte[] image;
	private @Lob byte[] icon;
	private String productivity;
	@Column(length=10485760)
	private String benefits;
	private int sequence;
	private @ManyToOne ProductType type;
	private @ManyToOne SaleType saleType;
	private @ManyToMany(targetEntity=Material.class) List<Material> materials;
	private @OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true) List<ProductPrice> prices;

	public String getDetails() {
		String lnsp = System.getProperty("line.separator");
		StringBuilder dt = new StringBuilder();
		if (StringUtils.isNotEmpty(description)) dt.append(description).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(usefulVarieties)) dt.append("ছাদের উপযোগী জাতঃ").append(lnsp).append(usefulVarieties).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(suitableContainer)) dt.append("উপযোগী পাত্রঃ").append(lnsp).append(suitableContainer).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(suitableTime)) dt.append("লাগানোর উপযুক্ত সময়ঃ").append(lnsp).append(suitableTime).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(landPreparation)) dt.append("মাটি তৈরিঃ").append(lnsp).append(landPreparation).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(seedlingType)) dt.append("চারার ধরণঃ").append(lnsp).append(seedlingType).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(careness)) dt.append("পরিচর্যাঃ").append(lnsp).append(careness).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(place)) dt.append("স্হানঃ").append(lnsp).append(place).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(hormone)) dt.append("হরমোনঃ").append(lnsp).append(hormone).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(pesticides)) dt.append("কীটনাশক বা ছত্রাকনাশকঃ").append(lnsp).append(pesticides).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(caution)) dt.append("সতর্কতাঃ").append(lnsp).append(caution).append(lnsp).append(lnsp);
		return dt.toString();
	}
}
