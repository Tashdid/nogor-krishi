package com.niil.nogor.krishi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.Comment;
import com.niil.nogor.krishi.entity.Suggestion;
import com.niil.nogor.krishi.repo.CommentRepo;
import com.niil.nogor.krishi.repo.SuggestionRepo;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 16, 2018
 *
 */
@Controller
@RequestMapping(SuggestionController.URL)
public class SuggestionController extends AbstractController {
	static final String URL = "/suggestions";

	@Value("${question.autopublish:true}")
	private Boolean questionAutoPublish;

	@Value("${comment.autopublish:true}")
	private Boolean commentAutoPublish;

	@Autowired SuggestionRepo suggestionRepo;
	@Autowired CommentRepo commentRepo;

	@ModelAttribute("suggestions")
	public List<Suggestion> suggestions() {
		return suggestionRepo.findTop10ByPublishedTrueOrderByViewCountDesc();
	}

	@RequestMapping
	public String suggestionsPage(ModelMap model) {
		model.addAttribute("allSuggestions", suggestionRepo.findAllByPublishedTrueOrderByCreatedOnDesc());
		return "suggestion/suggestions";
	}

	@RequestMapping(value = {"/{suggestion}", "/{suggestion}/", "/{suggestion}/{title}"})
	public String suggestionPage(@PathVariable Suggestion suggestion, ModelMap model) {
		model.addAttribute("suggestion", suggestion);
		suggestion.incrementViewCount();
		suggestionRepo.save(suggestion);
		return "suggestion/suggestion";
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/entry")
	public String newSuggestionPage(ModelMap model) {
		model.addAttribute("suggestion", new Suggestion());
		return "suggestion/newsuggestion";
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value="/entry", method=RequestMethod.POST)
	public String saveNewSuggestion(@Valid Suggestion suggestion, BindingResult bindingResult, final ModelMap model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
			model.addAttribute("suggestion", suggestion);
			return "suggestion/newsuggestion";
		}
		suggestion.setUser(securityService.findLoggedInUser());
		suggestion.setPublished(questionAutoPublish);
		suggestionRepo.save(suggestion);
		return "redirect:/suggestions/" + suggestion.getId() + "/" + suggestion.getTitle();
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value="/{suggestion}/comment", method=RequestMethod.POST)
	public String saveComment(@PathVariable Suggestion suggestion,
			String comment, final ModelMap model) {
		String pageToRedirect = "redirect:/suggestions/" + suggestion.getId() + "/" + suggestion.getTitle();
		if (StringUtils.isEmpty(comment)) return pageToRedirect;
		Comment comm = new Comment();
		comm.setComment(comment);
		comm.setUser(securityService.findLoggedInUser());
		comm.setPublished(commentAutoPublish);
		comm.setSuggestion(suggestion);
		commentRepo.save(comm);
		return pageToRedirect;
	}

	@ResponseBody
	@RequestMapping("/search")
	public List<Map<String, Object>> searchSuggestions(@RequestParam String term) {
		return suggestionRepo.findByTitleIgnoreCaseContaining(term).stream()
					.map(s -> {
						Map<String, Object> lst = new HashMap<>();
						lst.put("key", s.getId());
						lst.put("value", s.getTitle());
						return lst;
					}).collect(Collectors.toList());
	}
}
