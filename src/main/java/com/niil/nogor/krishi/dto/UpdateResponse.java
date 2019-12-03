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
public class UpdateResponse implements Serializable {
	ProductProperty objJson;
	Boolean isError;
	String errorMsg;

}
