package com.niil.nogor.krishi.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niil.nogor.krishi.entity.Role;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.NurseryRepo;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.util.AppUtil;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 23, 2018
 *
 */
@Controller
@RequestMapping(NurseryUserController.URL)
public class NurseryUserController extends AbstractController {
	static final String URL = "/manage/user";

	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Autowired AppUtil appUtil;
	@Autowired UserRepo userRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired UserController userController;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(new User(), model);
	}

	@RequestMapping(value="/{user}")
	public String updateScreen(@PathVariable User user, ModelMap model) {
		model.addAttribute("bean", user);
		model.addAttribute("brand", "Nursery User");
		model.addAttribute("allbeans", userRepo.findAllByNurseryIsNotNull());
		model.addAttribute("nurseries", nurseryRepo.findAll());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid User user, BindingResult bindingResult, final ModelMap model) throws IOException {
		User bean;
		if (user.getId() == null || (bean = userRepo.findOne(user.getId())) == null) {
			if (!userController.isValidUser(user, bindingResult, model)) {
				return updateScreen(user, model);
			} else if (user.getNursery() == null) {
				model.addAttribute("error", "নার্সারি সিলেক্ট করুন");
				return updateScreen(user, model);
			}
			bean = User.builder()
						.mobile(appUtil.sanitizeMobileNumber(user.getMobile()))
						.password(bCryptPasswordEncoder.encode(user.getPassword()))
						.nursery(user.getNursery())
						.build();
		} else if (StringUtils.isNotEmpty(user.getPassword())) {
			if (!userController.hasPassExtrenth(user, model)) {
				user.setNursery(bean.getNursery());
				user.setMobile(bean.getMobile());
				return updateScreen(user, model);
			}
			bean.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		}
		bean.setName(user.getName());
		bean.setDisabled(user.isDisabled());
		bean.setEmail(user.getEmail());
		bean.setRole(Role.VENDOR);
		bean = userRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		userRepo.delete(code);
		return true;
	}
}
