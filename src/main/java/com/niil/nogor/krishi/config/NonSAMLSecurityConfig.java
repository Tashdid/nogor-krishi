package com.niil.nogor.krishi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Profile("!saml")
@EnableWebSecurity
public class NonSAMLSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("ngkrishiUser!123").roles("USER").and()
				.withUser("admin").password("ngkrishiAdmin@321").roles("USER", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().and()
			.authorizeRequests()
				.antMatchers("/manage/**", "/rest/**").fullyAuthenticated()
				.anyRequest().permitAll().and()
			.csrf().disable();
	}
}
