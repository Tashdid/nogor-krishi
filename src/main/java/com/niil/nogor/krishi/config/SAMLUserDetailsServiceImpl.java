package com.niil.nogor.krishi.config;

import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.entity.Role;
import com.niil.nogor.krishi.repo.UserRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jul 15, 2018
 *
 */
@Component
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	@Value("#{'${saml.admin.users:01717314837,01753420663,01712927449}'.split(',')}")
	List<String> adminMobiles;

	@Autowired private UserRepo userRepo;

	public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
		String ssoID = credential.getAuthenticationAssertion().getSubject().getSubjectConfirmations().get(0).getSubjectConfirmationData().getInResponseTo();
		LOG.info("######################### SAML attributes ###############################");
		LOG.info("ssoid = {}", ssoID);
		LOG.info("========== All attributes =============");
		for(Attribute at : credential.getAttributes()) {
			LOG.info("{} = {}", at.getName(), credential.getAttributeAsString(at.getName()));
		}
		LOG.info("######################### SAML attributes ###############################");

		String mobile = credential.getAttributeAsString("username");

		com.niil.nogor.krishi.entity.User user = userRepo.findByMobile(mobile);
		if (user == null) {
			user = new com.niil.nogor.krishi.entity.User();
			user.setMobile(mobile);
			user.setEmail(credential.getAttributeAsString("email"));
			user.setName(credential.getAttributeAsString("name"));
			user.setPassword("<makehappy>");
			user.setIdpUser(true);
			user.setRole(adminMobiles.contains(mobile) ? Role.ADMIN : Role.valueOf(credential.getAttributeAsString("groups").toUpperCase()));

			userRepo.save(user);
		} else if (!user.isIdpUser()) 
			throw new UsernameNotFoundException("Conflicting credentials! Contact with Administrator.");
		// In a real scenario, this implementation has to locate user in a arbitrary
		// dataStore based on information present in the SAMLCredential and
		// returns such a date in a form of application specific UserDetails object.
		return User.withUsername(user.getMobile())
					.roles(user.getRole().name())
					.password("<makehappy>").build();
	}
}
