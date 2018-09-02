package com.niil.nogor.krishi.view;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jun 3, 2018
 *
 */
@Component("e2bconv")
public class EngToBengali {
	private Map<Character, String> mapping = new HashMap<>();
	
	@PostConstruct
	private void init() {
		mapping.put('0', "০");
		mapping.put('1', "১");
		mapping.put('2', "২");
		mapping.put('3', "৩");
		mapping.put('4', "৪");
		mapping.put('5', "৫");
		mapping.put('6', "৬");
		mapping.put('7', "৭");
		mapping.put('8', "৮");
		mapping.put('9', "৯");
		mapping.put('.', ".");
	}

	public String getBengali(Object obj) {
		if (obj == null) return null;
		String vl = obj.toString();
		String rsp = "";
		for (int i = 0; i < vl.length(); i++) {
			char c = vl.charAt(i);
			rsp += mapping.containsKey(c) ? mapping.get(c) : c;
		}
		return rsp;
	}

	public String getDateAsBangla(String date) {
		String bng = getBengali(date);
		bng = bng.replace("January", "জানুয়ারি");
		bng = bng.replace("February", "ফেব্রুয়ারি");
		bng = bng.replace("March", "মার্চ");
		bng = bng.replace("April", "এপ্রিল");
		bng = bng.replace("May", "মে");
		bng = bng.replace("June", "জুন");
		bng = bng.replace("July", "জুলাই");
		bng = bng.replace("August", "আগস্ট");
		bng = bng.replace("September", "সেপ্টেম্বর");
		bng = bng.replace("October", "অক্টোবর");
		bng = bng.replace("November", "নভেম্বর");
		bng = bng.replace("December", "ডিসেম্বর");
		bng = bng.replace("AM", "পূর্বাহ্ণ");
		bng = bng.replace("PM", "অপরাহ্ন");
		bng = bng.replace("am", "পূর্বাহ্ণ");
		bng = bng.replace("pm", "অপরাহ্ন");
		return bng;
	}
}
