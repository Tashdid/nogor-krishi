package com.niil.nogor.krishi.idp;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.view.UserDetails;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jul 15, 2018
 *
 */
@Component
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {
	
	// Logger
	private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);
	
	public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
		
		// The method is supposed to identify local account of user referenced by
		// data in the SAML assertion and return UserDetails object describing the user.
		
		String userID = credential.getNameID().getValue();
		
		String ssoID = credential.getAuthenticationAssertion().getSubject().getSubjectConfirmations().get(0).getSubjectConfirmationData().getInResponseTo();
		
		LOG.info("######################### SAML attributes ###############################");
		LOG.info("username = {}", credential.getNameID().getValue());
		LOG.info("ssoid = {}", ssoID);
		LOG.info("========== All attributes =============");
		for(Attribute at : credential.getAttributes()) {
			LOG.info("{} = {}", at.getName(), credential.getAttributeAsString(at.getName()));
		}
		LOG.info("######################### SAML attributes ###############################");
		
		LOG.info(userID + " is logged in");
		List<GrantedAuthority> authorities = new ArrayList<>();
		GrantedAuthority authority = new SimpleGrantedAuthority(credential.getAttributeAsString("groups"));
		authorities.add(authority);

		// In a real scenario, this implementation has to locate user in a arbitrary
		// dataStore based on information present in the SAMLCredential and
		// returns such a date in a form of application specific UserDetails object.
		return new UserDetails(credential.getAttributeAsString("username"), credential.getAttributeAsString("name"), credential.getAttributeAsString("email"), credential.getAttributeAsString("groups"));
	}
}
