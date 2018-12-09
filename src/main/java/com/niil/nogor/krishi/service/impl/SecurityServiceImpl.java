package com.niil.nogor.krishi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public String findLoggedInUsername() {
		Authentication ath = SecurityContextHolder.getContext().getAuthentication();
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

	@Override
	public void autologin(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, password, userDetails.getAuthorities());

		authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			logger.debug(String.format("Auto login %s successfully!", username));
		}
	}
}
