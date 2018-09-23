package com.niil.nogor.krishi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.entity.Role;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.service.SecurityService;
import com.niil.nogor.krishi.util.AppUtil;
import com.niil.nogor.krishi.view.ChangePassword;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 30, 2018
 *
 */
@Controller
public class UserController extends AbstractController {

	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Autowired private AppUtil appUtil;
	@Autowired private UserRepo userRepo;
	@Autowired private SecurityService securityService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(@RequestParam(required=false) String error,
			@RequestParam(required=false) String logout, final ModelMap model) {
		model.addAttribute("error", error);
		if (error != null)
			model.addAttribute("error", "কোন তথ্য পাওয়া যায় নাই");
		return logout == null ? "user/login" : "redirect:/";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationPage(final ModelMap model) {
		model.addAttribute("user", new User());
		return "user/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registration(@Valid User user, BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttrs) {
		model.addAttribute("user", user);
		if (!isValidUser(user, bindingResult, model)) {
			return "user/register";
		}

		user.setMobile(appUtil.sanitizeMobileNumber(user.getMobile()));
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole(Role.GARDENER);
		userRepo.save(user);

		redirectAttrs.addFlashAttribute("msg", "আপনার একাউন্ট তৈরি হয়েছে!");
		return "redirect:/login";
	}

	public boolean isValidUser(User user, BindingResult bindingResult, final ModelMap model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return false;
		}
		String sntmb = appUtil.sanitizeMobileNumber(user.getMobile());
		if (!appUtil.isValidMobile(sntmb)) {
			model.addAttribute("error", "দয়া করে সঠিক মোবাইল নং প্রদান করুন");
			return false;
		} else if (userRepo.findByMobile(sntmb) != null) {
			model.addAttribute("error", "দুঃখিত, এই মোবাইল নং দিয়ে একজন নিবন্ধিত আছেন।");
			return false;
		}

		return hasPassExtrenth(user, model);
	}

	public boolean hasPassExtrenth(User user, final ModelMap model) {
		Zxcvbn passwordCheck = new Zxcvbn();
		Strength strength = passwordCheck.measure(user.getPassword());

		if (strength.getScore() < 3) {
			model.addAttribute("error", "আপনার পাসওয়ার্ড খুব দুর্বল!");
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public String changePasswordPage(final ModelMap model) {
		return "user/changepassword";
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public String changePassword(@Valid ChangePassword form, BindingResult bindingResult, final ModelMap model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "user/changepassword";
		}

		User user = securityService.findLoggedInUser();
		if (!bCryptPasswordEncoder.matches(form.getPassword(), user.getPassword())) {
			model.addAttribute("error", "ভূল পাসওয়ার্ড");
			return "user/changepassword";
		}

		Zxcvbn passwordCheck = new Zxcvbn();
		Strength strength = passwordCheck.measure(form.getNewPassword());

		if (strength.getScore() < 3) {
			model.addAttribute("error", "আপনার পাসওয়ার্ড খুব দুর্বল!");
			return "user/changepassword";
		}

		user.setPassword(bCryptPasswordEncoder.encode(form.getNewPassword()));
		userRepo.save(user);

		model.addAttribute("msg", "আপনার পাসওয়ার্ড পরিবর্তন হয়েছে!");
		return "user/changepassword";
	}
}
