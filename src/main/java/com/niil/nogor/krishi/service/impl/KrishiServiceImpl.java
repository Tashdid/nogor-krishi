package com.niil.nogor.krishi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.niil.nogor.krishi.service.KrishiService;
import com.niil.nogor.krishi.service.model.FarmerResponse;
import com.niil.nogor.krishi.service.model.ServiceRegisteredUser;

import krishi.gov.api.KrishiAPI;
import krishi.gov.api.models.APIResponse;
import krishi.gov.api.models.BasicData;
import krishi.gov.api.models.Block;
import krishi.gov.api.models.Farmer;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 9, 2018
 *
 */
@Component
public class KrishiServiceImpl implements KrishiService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${farmers.api.url:http://27.147.142.170:8080/robiaiapi}")
	private String url;

	@Autowired KrishiAPI krishiAPI;

	@Override
	public ServiceRegisteredUser getServiceRegisteredUser(String mobile) {
		ResponseEntity<FarmerResponse> re = restTemplate.postForEntity(url + "/get_farmers?mobile=" + mobile, null, FarmerResponse.class);
		if (re == null || !re.hasBody() || re.getBody().getFarmers() == null || re.getBody().getFarmers().isEmpty()) return null;
		Farmer farmer = re.getBody().getFarmers().get(0);
		return farmer.getBlockId() == null ? null : getServiceRegisteredUser(farmer.getBlockId());
	}

	@Override
	public void addServiceRegisteredUser(String mobile, ServiceRegisteredUser serviceRegisteredUser) {
		ServiceRegisteredUser user = getServiceRegisteredUser(mobile);
		String rq = "/update_farmers?mobile=" + mobile + "&" + serviceRegisteredUser.getUpdateRequest();
		if (user == null) {
			rq = "/add_farmers?mobile=" + mobile + "&" + serviceRegisteredUser.getAddRequest();
		}
		restTemplate.postForEntity(url + rq, null, FarmerResponse.class);
	}

	private String getName(APIResponse<BasicData> response) {
		return response.getData() == null ? "" : response.getData().get(0).getName();
	}

	private ServiceRegisteredUser getServiceRegisteredUser(Integer blockId) {
		ServiceRegisteredUser serviceRegisteredUser = new ServiceRegisteredUser();
		APIResponse<Block> brs = krishiAPI.getBlockDetails(blockId);
		if (brs == null || brs.getData() == null || brs.getData().isEmpty()) return null;
		Block block = brs.getData().get(0);
		APIResponse<BasicData> dt = krishiAPI.getAreaDetails(block.getDivisionId());
		serviceRegisteredUser.setDivision(getName(dt));
		serviceRegisteredUser.setDivisionId(block.getDivisionId());
		dt = krishiAPI.getAreaDetails(block.getDistrictId());
		serviceRegisteredUser.setDistrict(getName(dt));
		serviceRegisteredUser.setDistrictId(block.getDistrictId());
		dt = krishiAPI.getAreaDetails(block.getUpazilaId());
		serviceRegisteredUser.setUpazila(getName(dt));
		serviceRegisteredUser.setUpazilaId(block.getUpazilaId());
		dt = krishiAPI.getUnionDetails(block.getUnionId());
		serviceRegisteredUser.setUnion(block.getUnionName());
		serviceRegisteredUser.setUnionId(block.getUnionId());
		serviceRegisteredUser.setBlock(block.getName());
		serviceRegisteredUser.setBlockId(blockId);
		return serviceRegisteredUser;
	}
}
