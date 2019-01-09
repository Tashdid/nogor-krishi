package com.niil.nogor.krishi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.niil.nogor.krishi.entity.APIContent;
import com.niil.nogor.krishi.repo.APIContentRepo;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jan 5, 2019
 *
 */
@Controller
@RequestMapping("/contents")
public class APIContentController extends AbstractController {

	@Autowired private APIContentRepo contentRepo;

	@ModelAttribute("contents")
	public List<APIContent> contents() {
		return contentRepo.findTop10ByPublishedTrueOrderBySequenceAsc();
	}

	@RequestMapping(value = {"/{content}", "/{content}/", "/{content}/{title}"})
	public String contentPage(@PathVariable APIContent content, @RequestParam(required=false) Boolean manage, ModelMap model) {
		model.addAttribute("content", content);
		return manage == null || !manage ? "contents/content" : "contents/content :: contentfrag";
	}

	@RequestMapping("/all")
	public String allContentsPage(ModelMap model) {
		model.addAttribute("allContents", contentRepo.findAllByPublishedTrueOrderBySequenceAsc());
		return "contents/all";
	}

	@RequestMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String contentsPage(ModelMap model) {
		model.addAttribute("published", contentRepo.findAllByPublishedTrue());
		model.addAttribute("allcontents", contentRepo.findAll());
		return "contents/contents";
	}

	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value ="/{content}/toggle", method = RequestMethod.POST)
	public Boolean contentPublishUnpublish(@PathVariable APIContent content) {
		boolean pubstat = !content.isPublished();
		content.setPublished(pubstat);
		if (pubstat) {
			content.setSequence(1);
			contentRepo.findTopByOrderBySequenceDesc().ifPresent(c -> content.setSequence(c.getSequence() + 1));
		} else {
			content.setSequence(0);
			int i = 1;
			for (APIContent cn : contentRepo.findAllByPublishedTrueOrderBySequenceAsc()) {
				if (cn.getId().equals(content.getId())) continue;
				cn.setSequence(i++);
				contentRepo.save(cn);
			}
		}
		cacheRepo.removeAllContents();
		return contentRepo.save(content).isPublished();
	}

	@RequestMapping("/pubtable")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String loadPublishedTable(ModelMap model) {
		model.addAttribute("published", contentRepo.findAllByPublishedTrue());
		return "contents/contents :: pubtablefrag";
	}

	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/table", method=RequestMethod.POST)
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			contentRepo.save(updater.getData().entrySet().stream().map(e -> {
				APIContent bean = contentRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}
}
