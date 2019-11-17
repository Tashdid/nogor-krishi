package com.niil.nogor.krishi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 2, 2018
 *
 */
@EnableWebSecurity
public class MultiHttpSecurityCustomConfig {
	
	@Autowired UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		auth
		.inMemoryAuthentication()
			.withUser("apiuser").password("ngkrishiAPIUser@!32123").roles("API");
	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/rest/**").authorizeRequests().anyRequest().hasRole("API").and().httpBasic();
		}
	}

	@Configuration
	@Order(2)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Bean
		public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
			return new MySimpleUrlAuthenticationSuccessHandler();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.authorizeRequests()
			.antMatchers("/dashboard/**", "/manage/**", "/settings/**").hasAnyRole("ADMIN")
			.antMatchers("/vendorprice", "/nursery/**").hasAnyRole("VENDOR")
			.antMatchers("/exlayout/**", "/serviceregister").fullyAuthenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.successHandler(myAuthenticationSuccessHandler())
				.permitAll()
			.and()
				.csrf().disable();
		}

	}
}
