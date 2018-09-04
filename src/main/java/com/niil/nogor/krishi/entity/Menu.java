package com.niil.nogor.krishi.entity;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 5, 2018
 *
 */
public enum Menu {
	PRODUCT("পণ্য"), SERVICE("সেবা"), SUGGESTION("পরামর্শ"), ABOUTUS("আমাদের সম্পর্কে"), CONTACT("যোগাযোগ");

	private String title;

	private Menu(String title) {
		this.title = title;
	}

	public String title() {return title;}
}
