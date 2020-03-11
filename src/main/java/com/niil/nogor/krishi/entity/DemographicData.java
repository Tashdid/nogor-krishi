package com.niil.nogor.krishi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class DemographicData {
	private @Id @GeneratedValue Long id;
	private @Column(nullable=false) String name;
	private Byte type;
	private Long parentId; 
	private int sequence;
	
	@Transient
	private String typeSt;
	@Transient
	private String typeStringForTable;
	@Transient
	private String parentBivagId;
		
}
