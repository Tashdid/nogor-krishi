package com.niil.nogor.krishi.view;

import org.springframework.web.multipart.MultipartFile;

import com.niil.nogor.krishi.entity.ProductType;

import lombok.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since May 1, 2018
 *
 */
@Getter
@Setter
public class ProductTypeForm extends ProductType {
	private MultipartFile iconFile;
	private MultipartFile imageFile;
}
