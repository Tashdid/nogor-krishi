package com.niil.nogor.krishi.view;

import org.springframework.web.multipart.MultipartFile;

import com.niil.nogor.krishi.entity.Product;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since May 1, 2018
 *
 */
@Getter
@Setter
public class ProductForm extends Product {
	private MultipartFile iconFile;
	private MultipartFile imageFile;
}
