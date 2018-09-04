package com.niil.nogor.krishi.view;

import org.springframework.stereotype.Component;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 5, 2018
 *
 */
@Component("vutil")
public class ViewUtil {

	public String titleToPath(String title) {
		return title.replaceAll("\\s+", "_");
	}
}
