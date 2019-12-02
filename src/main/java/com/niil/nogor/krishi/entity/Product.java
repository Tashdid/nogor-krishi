package com.niil.nogor.krishi.entity;

import java.util.ArrayList;
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
	private @ManyToOne Product parent;

	@OneToMany(targetEntity=Product.class, mappedBy="parent", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Product> childProductList;

	public String getDetails() {
		String lnsp = System.getProperty("line.separator");
		StringBuilder dt = new StringBuilder();
		if (StringUtils.isNotEmpty(description)||StringUtils.isNotEmpty(parent.description)) dt.append(StringUtils.isNotEmpty(description)?description:parent.description).append(lnsp).append(lnsp);
		if (StringUtils.isNotEmpty(benefits)) dt.append("গুণাগুণঃ").append(lnsp).append(benefits).append(lnsp).append(lnsp);
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
		if (StringUtils.isNotEmpty(productivity)) dt.append("ফলনঃ").append(lnsp).append(productivity).append(lnsp).append(lnsp);
		return dt.toString();
	}

	public List<String> getDetailsList() {
		List<String> dtlst = new ArrayList<>();
		if (StringUtils.isNotEmpty(description)||(parent!=null && StringUtils.isNotEmpty(parent.description))) {
			dtlst.add(StringUtils.isNotEmpty(description)?description:parent.description);
			dtlst.add("");
			dtlst.add("");
		}
		checkAndaddItem(dtlst, "গুণাগুণঃ", StringUtils.isEmpty(benefits)&&parent!=null?parent.benefits:benefits);
		checkAndaddItem(dtlst, "ছাদের উপযোগী জাতঃ", StringUtils.isEmpty(usefulVarieties)&&parent!=null?parent.usefulVarieties:usefulVarieties);
		checkAndaddItem(dtlst, "উপযোগী পাত্রঃ", StringUtils.isEmpty(suitableContainer)&&parent!=null?parent.suitableContainer:suitableContainer);
		checkAndaddItem(dtlst, "লাগানোর উপযুক্ত সময়ঃ", StringUtils.isEmpty(suitableTime)&&parent!=null?parent.suitableTime:suitableTime);
		checkAndaddItem(dtlst, "মাটি তৈরিঃ", StringUtils.isEmpty(landPreparation)&&parent!=null?parent.landPreparation:landPreparation);
		checkAndaddItem(dtlst, "চারার ধরণঃ", StringUtils.isEmpty(seedlingType)&&parent!=null?parent.seedlingType:seedlingType);
		checkAndaddItem(dtlst, "পরিচর্যাঃ", StringUtils.isEmpty(careness)&&parent!=null?parent.careness:careness);
		checkAndaddItem(dtlst, "স্হানঃ", StringUtils.isEmpty(place)&&parent!=null?parent.place:place);
		checkAndaddItem(dtlst, "হরমোনঃ", StringUtils.isEmpty(hormone)&&parent!=null?parent.hormone:hormone);
		checkAndaddItem(dtlst, "কীটনাশক বা ছত্রাকনাশকঃ", StringUtils.isEmpty(pesticides)&&parent!=null?parent.pesticides:pesticides);
		checkAndaddItem(dtlst, "সতর্কতাঃ", StringUtils.isEmpty(caution)&&parent!=null?parent.caution:caution);
		checkAndaddItem(dtlst, "ফলনঃ", StringUtils.isEmpty(productivity)&&parent!=null?parent.productivity:productivity);
		if (dtlst.isEmpty()) dtlst.add("");
		return dtlst;
	}

	private void checkAndaddItem(List<String> dtlst, String title, String content) {
		if (StringUtils.isEmpty(content)) return;
		dtlst.add("<b>" + title + "</b>");
		dtlst.add(content);
	}
}
