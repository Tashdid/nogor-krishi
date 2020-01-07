package com.niil.nogor.krishi.service;

public interface MailService {

	public String sendEmail(String toUser, String subject, String msgText);
}
