package com.niil.nogor.krishi.controller;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.entity.PasswordResetToken;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.PasswordResetTokenRepo;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.view.PasswordForgotModel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 10, 2019
 *
 */
@Slf4j
@Controller
@RequestMapping("/forgotpassword")
public class PasswordForgotController extends AbstractController {
	private final String FP_PAGE = "user/forgotpassword";

	@Autowired private UserRepo userRepo;
	@Autowired private PasswordResetTokenRepo passwordResetTokenRepo;
	@Autowired private JavaMailSender emailSender;

	@ModelAttribute("forgotPasswordForm")
	public PasswordForgotModel forgotPasswordModel() {
		return new PasswordForgotModel();
	}

	@GetMapping
	public String displayForgotPasswordPage() {
		return FP_PAGE;
	}

	@PostMapping
	public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotModel form,
			BindingResult result) {
		if (result.hasErrors()) {
			return FP_PAGE;
		}
		User user = userRepo.findByEmail(form.getEmail());
		if (user == null) {
			result.rejectValue("email", null, "এই ই-মেইল দিয়ে কোনও ইউজার নিবন্ধিত নেই!");
			return FP_PAGE;
		}

		PasswordResetToken token = new PasswordResetToken();
		token.setToken(UUID.randomUUID().toString());
		token.setUser(user);
		token.setExpiryDate(30);
		passwordResetTokenRepo.save(token);

		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			helper.setTo(user.getEmail());
			helper.setText(getEmailBody(user, token.getToken()), true);
			helper.setSubject("Password Reset Request");
			helper.setFrom("noreply@nogorkrishi.com");

			emailSender.send(message);
		} catch (Exception e) {
			log.error("Failed to send email with error: {}", e.getMessage());
			throw new RuntimeException(e);
		}
		return "redirect:/forgotpassword?success";
	}

	private String getEmailBody(User user, String token) {
		StringBuilder bd = new StringBuilder();
		bd.append("<html><body><p>Dear ")
			.append(user.getName())
			.append(",</p><p>You have requested a password reset.<a href=\"http://nogorkrishi.com/resetpassword/")
			.append(token)
			.append("\">Reset your password</a></p><p>Thanks</p><p>নগরকৃষি প্রকল্প - Nogorkrishi Prokolpo</p>");
		return bd.toString();
	}
}
