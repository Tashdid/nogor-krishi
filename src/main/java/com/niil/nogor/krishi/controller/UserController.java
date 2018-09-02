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
import com.niil.nogor.krishi.util.AppUtil;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Aug 30, 2018
 *
 */
@Controller
public class UserController {

	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Autowired private AppUtil appUtil;
	@Autowired private UserRepo userRepo;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(@RequestParam(required=false) String error, final ModelMap model) {
		model.addAttribute("error", error);
		if (error != null)
			model.addAttribute("error", "কোন তথ্য পাওয়া যায় নাই");
		return "user/login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationPage(final ModelMap model) {
		model.addAttribute("user", new User());
		return "user/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registration(@Valid User user, BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttrs) {
		model.addAttribute("user", user);
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "user/register";
		}
		String sntmb = appUtil.sanitizeMobileNumber(user.getMobile());
		if (!appUtil.isValidMobile(sntmb)) {
			model.addAttribute("error", "দয়া করে সঠিক মোবাইল নং প্রদান করুন");
			return "user/register";
		} else if (userRepo.findByMobile(sntmb) != null) {
			model.addAttribute("error", "দুঃখিত, এই মোবাইল নং দিয়ে একজন নিবন্ধিত আছেন।");
			return "user/register";
		}

		Zxcvbn passwordCheck = new Zxcvbn();
		Strength strength = passwordCheck.measure(user.getPassword());

		if (strength.getScore() < 3) {
			model.addAttribute("error", "আপনার পাসওয়ার্ড খুব দুর্বল!");
			return "user/register";
		}

		user.setMobile(sntmb);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole(Role.GARDENER);
		userRepo.save(user);

		redirectAttrs.addFlashAttribute("msg", "আপনার একাউন্ট তৈরি হয়েছে!");
		return "redirect:/login";
	}
}
