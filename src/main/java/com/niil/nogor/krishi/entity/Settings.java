package com.niil.nogor.krishi.entity;

import javax.persistence.*;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
	private @Id @GeneratedValue Long id;
	private String blogEmail;
	private String nurseryEmail;
	@Column(length=10485760)
	private String footerText;
	private String ponnoMenu;
	private String nurseryMenu;
	private String suggestionMenu;
	private String photoGalleryMenu;
	private String photoGalleryTitle;
	private String gardenDesignMenu;
	private String gardenDesignTitle;
}
