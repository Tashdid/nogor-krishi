package com.niil.nogor.krishi.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.util.AppUtil;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 30, 2018
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired private AppUtil appUtil;
	@Autowired private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByMobile(appUtil.sanitizeMobileNumber(username));

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().name()));

		return new org.springframework.security.core.userdetails.User(user.getMobile(), user.getPassword(), grantedAuthorities);
	}

}
