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
@ToString(exclude={"image"})
public class Material {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private @Column(length=10485760) String description;
	private @Lob byte[] image;
	private int sequence;
	private @ManyToOne MaterialType type;

	public List<String> getDetailsList() {
		List<String> dtlst = new ArrayList<>();
		if (StringUtils.isNotEmpty(description)) {
			dtlst.add(description);
		}
		return dtlst;
	}	
}
