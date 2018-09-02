package com.niil.nogor.krishi.service;

import com.niil.nogor.krishi.entity.User;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 30, 2018
 *
 */
public interface SecurityService {

	public String findLoggedInUsername();

	public User findLoggedInUser();

	public void autologin(String username, String password);
}
