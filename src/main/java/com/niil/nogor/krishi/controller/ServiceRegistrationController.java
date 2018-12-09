package com.niil.nogor.krishi.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.config.Constants;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.service.KrishiService;
import com.niil.nogor.krishi.service.model.ServiceRegisteredUser;
import com.niil.nogor.krishi.view.Type;

import krishi.gov.api.KrishiAPI;
import krishi.gov.api.models.BasicData;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 9, 2018
 *
 */
@Controller
public class ServiceRegistrationController extends AbstractController {

	@Autowired private KrishiAPI krishiAPI;
	@Autowired private KrishiService krishiService;

	@RequestMapping("/serviceregister")
	public String page(@RequestParam(required=false) boolean isNewRegistration,
			HttpServletRequest request, final ModelMap model) {
		model.addAttribute("divisions", getList(Type.DIVISION, null));
		User user = securityService.findLoggedInUser();
		ServiceRegisteredUser sru = krishiService.getServiceRegisteredUser(user.getMobile());
		if (isNewRegistration) {
			if (sru == null)
				model.addAttribute("regmsg", "নিবন্ধনের জন্য ধন্যবাদ! ৩৩৩১ - এ সেবা নিতে চাইলে নিচের তথ্যগুলো দিন। ");
			else
				model.addAttribute("regmsg", "নিবন্ধনের জন্য ধন্যবাদ! আপনি ৩৩৩১ সেবার জন্য ইতিমধ্যেই নিবন্ধিত। পরিবর্তন করতে চাইলে পুনরায় তথ্য দিন।");
		}
		model.addAttribute("serviceuser", sru);
		Object ncgi = request.getSession().getAttribute(Constants.NEWLY_CREATED_GARDEN_ID);
		model.addAttribute("nexturl", ncgi == null ? "/exlayout/list" : ("/exlayout/" + ncgi));
		return "user/serviceregister";
	}

	@ResponseBody
	@RequestMapping(value = "/serviceregister", method = RequestMethod.POST)
	public String page(ServiceRegisteredUser serviceUser, final ModelMap model) {
		User user = securityService.findLoggedInUser();
		krishiService.addServiceRegisteredUser(user.getMobile(), serviceUser);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/areas/{type}", method = RequestMethod.GET)
	public List<BasicData> getList(@PathVariable Type type, @RequestParam(required=false) Integer id) {
		if (type != Type.DIVISION && id == null) return Arrays.asList();
		switch(type) {
			case DIVISION:
				List<BasicData> data = krishiAPI.getDivisions().getData();
				return data;
			case DISTRICT:
				return krishiAPI.getDistricts(id).getData();
			case UPAZILA:
				return krishiAPI.getUpazilas(id).getData();
			case UNION:
				return krishiAPI.getUnions(id).getData();
			default:
				return krishiAPI.getBlocks(id).getData();
		}
	}
}
