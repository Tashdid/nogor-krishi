package com.niil.nogor.krishi.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.niil.nogor.krishi.util.AppUtil;
import com.niil.nogor.krishi.view.ChangePassword;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 30, 2018
 *
 */
@Controller
public class UserController extends AbstractController {

	@Autowired private AppUtil appUtil;
	@Autowired private UserRepo userRepo;
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(@RequestParam(required=false) String error,
			@RequestParam(required=false) String logout, final ModelMap model) {
		model.addAttribute("error", error);
		if (error != null)
			model.addAttribute("error", "কোন তথ্য পাওয়া যায় নাই");
		return logout == null ? "user/login" : "redirect:/";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationPage(@RequestParam(required=false) String reqFrom,
			HttpServletRequest request, final ModelMap model) {
		model.addAttribute("reqFrom", reqFrom);
		model.addAttribute("user", new User());
		return "user/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registration(@Valid User user, BindingResult bindingResult,
			@RequestParam(required=false) String reqFrom, HttpServletRequest request,
			final ModelMap model, final RedirectAttributes redirectAttrs) {
		model.addAttribute("reqFrom", reqFrom);
		model.addAttribute("user", user);
		if (!isValidUser(user, bindingResult, model)) {
			return "user/register";
		}

		String passwd = user.getPassword();
		user.setMobile(appUtil.sanitizeMobileNumber(user.getMobile()));
		user.setPassword(bCryptPasswordEncoder.encode(passwd));
		user.setRole(Role.GARDENER);
		userRepo.save(user);

		try {
			request.login(user.getMobile(), passwd);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		redirectAttrs.addFlashAttribute("msg", "আপনার একাউন্ট তৈরি হয়েছে!");
		reqFrom = StringUtils.isNotEmpty(reqFrom) ? ("&reqFrom=" + reqFrom) : "";
		return "redirect:/serviceregister?isNewRegistration=true" + reqFrom;
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
		if (user.getPassword().length() < 6) {
			model.addAttribute("error", "৬ বা তার অধিক বর্ণের পাসওয়ার্ড দিন।");
			return false;
		}
		return true;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public String changePasswordPage(final ModelMap model) {
		return "user/changepassword";
	}

	@PreAuthorize("isAuthenticated()")
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

		if (form.getNewPassword().length() < 6) {
			model.addAttribute("error", "৬ বা তার অধিক বর্ণের পাসওয়ার্ড দিন।");
			return "user/changepassword";
		}

		user.setPassword(bCryptPasswordEncoder.encode(form.getNewPassword()));
		userRepo.save(user);

		model.addAttribute("msg", "আপনার পাসওয়ার্ড পরিবর্তন হয়েছে!");
		return "user/changepassword";
	}
}
