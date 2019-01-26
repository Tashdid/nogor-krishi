package com.niil.nogor.krishi.service.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 9, 2018
 *
 */
@Data
public class ServiceRegisteredUser {
	private String name;
	@NotNull
	private Integer divisionId;
	private String division;
	@NotNull
	private Integer districtId;
	private String district;
	@NotNull
	private Integer upazilaId;
	private String upazila;
	@NotNull
	private Integer unionId;
	private String union;
	@NotNull
	private Integer blockId;
	private String block;

	public String getDetails() {
		if (division == null) return "";
		return String.format("বিভাগঃ %s, জেলাঃ %s, উপজেলাঃ %s, ইউনিয়নঃ %s এবং ব্লকঃ %s", division, district, upazila, union, block);
	}

	public String getUpdateRequest() {
		return String.format("division_id=%d&district_id=%d&upzilla_id=%d&block_id=%d", divisionId, districtId, upazilaId, blockId);
	}

	public String getAddRequest() {
		return String.format("name=%s&%s", name, getUpdateRequest());
	}
}
