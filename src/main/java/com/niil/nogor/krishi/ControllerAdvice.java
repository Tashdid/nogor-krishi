package com.niil.nogor.krishi;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.niil.nogor.krishi.view.UserDetails;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jul 17, 2018
 *
 */
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	@ModelAttribute("loggeduser")
	public UserDetails sessionUser() {
		Authentication authn = SecurityContextHolder.getContext().getAuthentication();
		if (authn == null) return null;
		Object prncpl = authn.getPrincipal();
		if (prncpl instanceof String) return null;
		return (UserDetails) prncpl;
	}
}
