package com.niil.nogor.krishi.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.entity.PasswordResetToken;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.PasswordResetTokenRepo;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.view.ChangePassword;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 14, 2019
 *
 */
@Controller
@RequestMapping("/resetpassword")
public class ResetPasswordController extends AbstractController {
	private final String RP_PAGE = "user/resetpassword";

	@Autowired private UserRepo userRepo;
	@Autowired private PasswordResetTokenRepo tokenRepository;
	@Autowired private BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute("resetPasswordForm")
	public ChangePassword resetPasswordModel() {
		return new ChangePassword();
	}

	@GetMapping("/{token}")
	public String resetPasswordPage(@PathVariable String token, final ModelMap model) {
		PasswordResetToken tk = tokenRepository.findByToken(token);
		if (tk == null || tk.getExpiryDate().isBefore(LocalDateTime.now()))
			throw new RuntimeException("টোকেন পাওয়া যায় নাই!");
		model.addAttribute("token", token);
		return RP_PAGE;
	}

	@PostMapping("/{token}")
	public String resetPassword(@PathVariable String token, final ModelMap model,
			@ModelAttribute("forgotPasswordForm") @Valid ChangePassword form,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
			return RP_PAGE;
		}
		PasswordResetToken tk = tokenRepository.findByToken(token);
		if (tk == null || tk.getExpiryDate().isBefore(LocalDateTime.now()))
			throw new RuntimeException("টোকেন পাওয়া যায় নাই!");

		if (form.getNewPassword().length() < 6) {
			model.addAttribute("error", "৬ বা তার অধিক বর্ণের পাসওয়ার্ড দিন।");
			return RP_PAGE;
		}
		User user = tk.getUser();
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		userRepo.save(user);
		tokenRepository.delete(tk);
		return "redirect:/login?resetSuccess";
	}
}
