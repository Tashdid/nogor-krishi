package com.niil.nogor.krishi;

import org.springframework.stereotype.Component;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since May 31, 2018
 *
 */
@Component
public class AppConfig {
	private boolean copyProductList = true;

	public boolean isCopyProductList() {
		return copyProductList;
	}

	public void setCopyProductList(boolean copyProductList) {
		this.copyProductList = copyProductList;
	}
}
