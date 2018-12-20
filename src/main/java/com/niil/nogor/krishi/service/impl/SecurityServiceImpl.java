package com.niil.nogor.krishi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.service.SecurityService;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 30, 2018
 *
 */
@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public String findLoggedInUsername() {
		Authentication ath = SecurityContextHolder.getContext().getAuthentication();
		if (ath == null) return null;
		Object userDetails = ath.getPrincipal();
		if (userDetails instanceof org.springframework.security.core.userdetails.User) {
			return ((org.springframework.security.core.userdetails.User) userDetails).getUsername();
		}
		return null;
	}

	@Override
	public User findLoggedInUser() {
		String user = findLoggedInUsername();
		if (user == null) return null;
		return userRepo.findByMobile(user);
	}
}
