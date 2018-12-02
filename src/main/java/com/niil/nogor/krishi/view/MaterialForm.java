package com.niil.nogor.krishi.view;

import org.springframework.web.multipart.MultipartFile;

import com.niil.nogor.krishi.entity.Material;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 2, 2018
 *
 */
@Getter
@Setter
public class MaterialForm extends Material {
	private MultipartFile imageFile;
}
