package com.niil.nogor.krishi.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 2, 2018
 *
 */
@Component
public class AppUtil {
	private Map<String, Character> mapping = new HashMap<>();

	@PostConstruct
	private void init() {
		mapping.put("০", '0');
		mapping.put("১", '1');
		mapping.put("২", '2');
		mapping.put("৩", '3');
		mapping.put("৪", '4');
		mapping.put("৫", '5');
		mapping.put("৬", '6');
		mapping.put("৭", '7');
		mapping.put("৮", '8');
		mapping.put("৯", '9');
	}

	public String sanitizeMobileNumber(String vl) {
		if (vl == null) return null;
		vl = vl.replaceAll("\\s+", "");
		String rsp = "";
		for (int i = 0; i < vl.length(); i++) {
			String c = String.valueOf(vl.charAt(i));
			rsp += mapping.containsKey(c) ? mapping.get(c) : c;
		}
		if (rsp.startsWith("+")) rsp = rsp.substring(1);
		if (rsp.startsWith("88")) rsp = rsp.substring(2);
		if (rsp.startsWith("00")) rsp = rsp.substring(1);
		return rsp;
	}

	public boolean isValidMobile(String vl) {
		return NumberUtils.isNumber(vl) && vl.length() == 11 && 
				StringUtils.startsWithAny(vl, new String[] {"011", "015", "016", "017", "018", "019"});
	}
}
