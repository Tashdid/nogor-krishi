package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.niil.nogor.krishi.entity.Page;
import com.niil.nogor.krishi.repo.PageRepo;
import com.niil.nogor.krishi.view.ImageUploadResponse;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 4, 2018
 *
 */
@Controller
@RequestMapping("/pages")
public class PagesController {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired Path imagePath;
	@Autowired Path filePath;
	@Autowired PageRepo pageRepo;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.POST)
	public String savePage(@Valid Page page, BindingResult bindingResult, final ModelMap model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return editor(page, model);
		}
		return "redirect:/pages/editor/" + pageRepo.save(page).getId();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping({"/editor", "/editor/", "/editor/{page}"})
	public String editor(@PathVariable(required=false) Page page, final ModelMap model) {
		model.addAttribute("bean", page == null ? new Page() : page);
		model.addAttribute("pages", pageRepo.findAll());
		return "pages/editor";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseBody
	@RequestMapping(value="/editor/image", method=RequestMethod.POST)
	public ImageUploadResponse uploadImage(@RequestParam("file") MultipartFile file) {
		return uploadImageOrFile(file, imagePath, "serveImage");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseBody
	@RequestMapping(value="/editor/file", method=RequestMethod.POST)
	public ImageUploadResponse uploadFile(@RequestParam("file") MultipartFile file) {
		return uploadImageOrFile(file, filePath, "serveFile");
	}

	private ImageUploadResponse uploadImageOrFile(MultipartFile file, Path path, String serveMethod) {
		String filename = new StringBuilder(RandomStringUtils.random(5, true, true))
				.append("_")
				.append(StringUtils.cleanPath(file.getOriginalFilename()))
				.toString()
					.replaceAll("\\s+", "_");
		try {
			if (file.isEmpty()) {
				log.error("Uploaded empty file with name {}", filename);
				throw new RuntimeException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				log.error("Uploaded filename {} looks malformed! Ignoring upload.", filename);
				throw new RuntimeException("Cannot store file with relative path outside current directory " + filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, path.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			log.error("Failed to store {}", filename);
			throw new RuntimeException("Failed to store file " + filename, e);
		}
		return new ImageUploadResponse(MvcUriComponentsBuilder
				.fromMethodName(PagesController.class, serveMethod, filename).build().toString());
	}

	@RequestMapping({"/{page}/{title}"})
	public String servePage(@PathVariable Page page, final ModelMap model) {
		if (!page.isPublished()) {
			return "error";
		}
		model.addAttribute("page", page);
		pageRepo.save(page.incrementViewCount());
		model.addAttribute("topPages", pageRepo.findTop10ByPublishedTrueOrderByViewCountDesc());
		return "pages/page";
	}

	@ResponseBody
	@RequestMapping("/image/{filename:.+}")
	public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
		return serve(filename, imagePath);
	}

	@ResponseBody
	@RequestMapping("/file/{filename:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		return serve(filename, filePath);
	}

	private ResponseEntity<Resource> serve(String filename, Path path) {
		try {
			Path file = path.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (!resource.exists() || !resource.isReadable()) {
				log.error("File not found with name {}", filename);
				throw new RuntimeException("Could not read file: " + filename);

			}
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (MalformedURLException e) {
			log.error("Failed to read file {}", filename);
			throw new RuntimeException("Could not read file: " + filename, e);
		}
	}
}
