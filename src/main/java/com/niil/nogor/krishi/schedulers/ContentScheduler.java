package com.niil.nogor.krishi.schedulers;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.entity.APIContent;
import com.niil.nogor.krishi.entity.APIOfficer;
import com.niil.nogor.krishi.repo.APIContentRepo;
import com.niil.nogor.krishi.repo.APIOfficerRepo;

import krishi.gov.api.KrishiAPI;
import krishi.gov.api.models.APIResponse;
import krishi.gov.api.models.Content;
import krishi.gov.api.models.Officer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jan 5, 2019
 *
 */
@Slf4j
@Component
public class ContentScheduler {

	@Autowired private KrishiAPI krishiAPI;
	@Autowired private APIContentRepo contentRepo;
	@Autowired private APIOfficerRepo officerRepo;

	@PostConstruct
	private void init() {
		if (contentRepo.findAll().isEmpty())
			loadContents();
	}

	@Scheduled(cron="0 0 2 * * *") // Every day at 02:00 am
	private void loadContents() {
		log.debug("Loading contents scheduler starts at {}", LocalDateTime.now());
		List<Content> contents = new ArrayList<>();
		int page = 1;
		do {
			APIResponse<Content> resp = krishiAPI.getContents(50, page);
			contents.addAll(resp.getData().stream().filter(c -> "published".equals(c.getStatus())).collect(Collectors.toList()));
			if (resp.getSkip() + resp.getLimit() < resp.getTotal())
				page = resp.getPage() + 1;
			else page = 0;
		} while(page > 0);

		Map<Integer, APIOfficer> ofmap = new HashMap<>();
		List<APIContent> apiContents = contents.stream().map(c -> {
			APIContent ac = new APIContent();
			Optional<APIContent> coop = contentRepo.findByApiId(c.getId());
			if (coop.isPresent()) {
				ac = coop.get();
				ofmap.put(ac.getAuthor().getApiId(), ac.getAuthor());
			} else {
				APIOfficer apiOff = getAPIOfficer(c.getAuthorId(), ofmap);
				if (apiOff == null) return null;
				ac.setAuthor(apiOff);
				ac.setCreatedAt(c.getCreatedAt());
				ac.setApiId(c.getId());
			}
			ac.setContent(c.getContent());
			ac.setFeatureImage(c.getFeatureImage());
			ac.setTitle(c.getTitle());
			ac.setStatus(c.getStatus());
			ac.setUpdatedAt(c.getUpdatedAt());
			return contentRepo.save(ac);
		}).filter(Objects::nonNull).collect(Collectors.toList());
		log.info("Loading contents scheduler loaded/checked {} contents at {}", apiContents.size(), LocalDateTime.now());
		log.debug("Loading contents scheduler ends at {}", LocalDateTime.now());
	}

	private APIOfficer getAPIOfficer(int apiOfficerId, Map<Integer, APIOfficer> ofmap) {
		if (ofmap.containsKey(apiOfficerId)) {
			return ofmap.get(apiOfficerId);
		}

		Optional<APIOfficer> oop = officerRepo.findByApiId(apiOfficerId);
		if (oop.isPresent()) {
			APIOfficer apiOff = oop.get();
			ofmap.put(apiOfficerId, apiOff);
			return apiOff;
		}

		APIResponse<Officer> rsp = krishiAPI.getOfficer(apiOfficerId);
		if (rsp == null || rsp.getData() == null || rsp.getData().size() != 1) return null;


		Officer ofc = rsp.getData().get(0);
		APIOfficer apiOff = new APIOfficer();
		apiOff.setAddress(ofc.getAddress());
		apiOff.setApiId(apiOfficerId);
		apiOff.setAvatar(ofc.getAvatar());
		apiOff.setEmail(ofc.getEmail());
		apiOff.setMobile(ofc.getMobile());
		apiOff.setName(ofc.getName());
		apiOff.setRole(ofc.getRole());
		apiOff = officerRepo.save(apiOff);
		ofmap.put(apiOfficerId, apiOff);
		return apiOff;
	}
}
