package com.niil.nogor.krishi.service;

import org.springframework.stereotype.Service;

import com.niil.nogor.krishi.service.model.ServiceRegisteredUser;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 9, 2018
 *
 */
@Service
public interface KrishiService {

	public ServiceRegisteredUser getServiceRegisteredUser(String mobile);

	public void addServiceRegisteredUser(String mobile, ServiceRegisteredUser serviceRegisteredUser);
}
