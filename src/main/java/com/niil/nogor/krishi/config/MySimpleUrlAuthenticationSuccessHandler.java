package com.niil.nogor.krishi.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 13, 2018
 *
 */
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(request, authentication);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, Authentication authentication) {
		String reqFrom = request.getParameter("reqFrom");
		String redirectUrl = request.getParameter("redirect");
		System.out.println("redirect" + redirectUrl);

		if (StringUtils.isNotEmpty(reqFrom)) return reqFrom;

		Object ncgi = request.getSession().getAttribute(Constants.NEWLY_CREATED_GARDEN_ID);
		if (ncgi != null) {
			return "/exlayout/" + ncgi;
		}
		boolean isGardener = false;
		boolean isVendor = false;
		boolean isAdmin = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_GARDENER")) {
				isGardener = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_VENDOR")) {
				isVendor = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}

		if (isGardener) {
			if(redirectUrl != null && !redirectUrl.isEmpty())
				return redirectUrl;
			else
			return "/exlayout/list";

		} else if (isVendor) {
			return "/vendorprice";
		} else if (isAdmin) {
			return "/manage";
		} else {
			return "/";
		}
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
}
