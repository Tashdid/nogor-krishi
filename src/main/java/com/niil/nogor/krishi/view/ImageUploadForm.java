package com.niil.nogor.krishi.view;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
@Data
public class ImageUploadForm {
	private Long id;
	private String details;
	private MultipartFile imageFile;
}
