package com.niil.nogor.krishi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.service.KrishiService;
import com.niil.nogor.krishi.service.model.ServiceRegisteredUser;

import krishi.gov.api.KrishiAPI;
import krishi.gov.api.models.APIResponse;
import krishi.gov.api.models.BasicData;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 9, 2018
 *
 */
@Component
public class KrishiServiceImpl implements KrishiService {

	private final Map<String, ServiceRegisteredUser> users = new HashMap<>();
	
	@Autowired KrishiAPI krishiAPI;

	@Override
	public ServiceRegisteredUser getServiceRegisteredUser(String mobile) {
		return users.get(mobile);
	}

	@Override
	public void addServiceRegisteredUser(String mobile, ServiceRegisteredUser serviceRegisteredUser) {
		APIResponse<BasicData> dt = krishiAPI.getAreaDetails(serviceRegisteredUser.getDivisionId());
		serviceRegisteredUser.setDivision(getName(dt));
		dt = krishiAPI.getAreaDetails(serviceRegisteredUser.getDistrictId());
		serviceRegisteredUser.setDistrict(getName(dt));
		dt = krishiAPI.getAreaDetails(serviceRegisteredUser.getUpazilaId());
		serviceRegisteredUser.setUpazila(getName(dt));
		dt = krishiAPI.getUnionDetails(serviceRegisteredUser.getUnionId());
		serviceRegisteredUser.setUnion(getName(dt));
		dt = krishiAPI.getBlockDetails(serviceRegisteredUser.getBlockId());
		serviceRegisteredUser.setBlock(getName(dt));

		users.put(mobile, serviceRegisteredUser);
	}

	private String getName(APIResponse<BasicData> response) {
		return response.getData() == null ? "" : response.getData().get(0).getName();
	}
}
