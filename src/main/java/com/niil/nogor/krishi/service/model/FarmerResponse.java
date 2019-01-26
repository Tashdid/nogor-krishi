package com.niil.nogor.krishi.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import krishi.gov.api.models.Farmer;
import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jan 26, 2019
 *
 */
@Data
public class FarmerResponse {
	private String errorCode;
	private String errorMessage;
	@JsonProperty("farmer_list")
	private List<Farmer> farmers;
}
