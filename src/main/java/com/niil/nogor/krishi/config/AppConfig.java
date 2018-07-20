package com.niil.nogor.krishi.config;

import org.springframework.stereotype.Component;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since May 31, 2018
 *
 */
@Component
public class AppConfig {
	private boolean copyProductList = false;

	public boolean isCopyProductList() {
		return copyProductList;
	}

	public void setCopyProductList(boolean copyProductList) {
		this.copyProductList = copyProductList;
	}
}
