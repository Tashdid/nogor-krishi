package com.niil.nogor.krishi.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jan 5, 2019
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIContent {
	private @Id @GeneratedValue Long id;
	protected int apiId;
	@Column(nullable=false, length=1048)
	private String title;
	@Column(nullable=false, length=10485760)
	private String content;
	private String featureImage;
	private String status;
	private boolean published;
	protected int sequence;
	protected LocalDateTime createdAt;
	protected LocalDateTime updatedAt;
	private @ManyToOne APIOfficer author;
}
