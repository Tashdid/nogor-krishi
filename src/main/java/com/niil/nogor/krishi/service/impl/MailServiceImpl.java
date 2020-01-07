package com.niil.nogor.krishi.service.impl;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.niil.nogor.krishi.service.MailService;


@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	JavaMailSender javaMail;
	
	@Async
	@Override
	public String sendEmail(String toUser, String subject, String msgText) {
		try {


			MimeMessage mimeMessage = javaMail.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			String htmlMsg = msgText;
			mimeMessage.setContent(htmlMsg, "text/html;charset=utf-8"); /** Use this or below line **/
//			helper.setText(htmlMsg, true); // Use this or above line.
			helper.setTo(toUser);
			helper.setSubject(subject);
			helper.setFrom("admin@nogorkrishi.com");
			javaMail.send(mimeMessage);
			
//			SimpleMailMessage msg = new SimpleMailMessage();
//			msg.setFrom("admin@nogorkrishi.com");
//	        msg.setTo(toUser);
//
//	        msg.setSubject(subject);
//	        msg.setText(msgText);
//	        
//	        javaMail.send(msg);
	        
	        
		}catch(Exception ex) {ex.printStackTrace();}
		return "";
	}
	
}
