package com.niil.nogor.krishi.dto;

import java.io.Serializable;

import com.niil.nogor.krishi.entity.ProductProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class UpdateResponse<E> implements Serializable {
	E objJson;
	Boolean isEdit;
	Boolean isError;
	String errorMsg;

}
