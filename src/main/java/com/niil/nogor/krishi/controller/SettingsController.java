package com.niil.nogor.krishi.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niil.nogor.krishi.config.AppConfig;
import com.niil.nogor.krishi.entity.CarouselImages;
import com.niil.nogor.krishi.entity.GalleryImages;
import com.niil.nogor.krishi.entity.ImageFile;
import com.niil.nogor.krishi.entity.Settings;
import com.niil.nogor.krishi.repo.CarouselImagesRepo;
import com.niil.nogor.krishi.repo.GalleryImagesRepo;
import com.niil.nogor.krishi.repo.ImageFileRepo;
import com.niil.nogor.krishi.view.ImageUploadForm;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Oct 12, 2018
 *
 */
@Controller
@RequestMapping(SettingsController.URL)
public class SettingsController extends AbstractController {
	static final String URL = "/settings";

	@Autowired ImageFileRepo imageFileRepo;
	@Autowired CarouselImagesRepo carouselImagesRepo;
	@Autowired GalleryImagesRepo galleryImagesRepo;

	@RequestMapping
	public String settingsPage(ModelMap model) {
		model.addAttribute("cbeans", carouselImagesRepo.findAll());
		model.addAttribute("gbeans", galleryImagesRepo.findAll());
		return URL.substring(1);
	}

	@RequestMapping(method=RequestMethod.POST)
	public String saveSettings(@Valid Settings settings) throws IOException {
		Settings st = settingsRepo.findAll().stream().findFirst().orElse(new Settings());
		st.setBlogEmail(settings.getBlogEmail());
		st.setNurseryEmail(settings.getNurseryEmail());
		st.setFooterText(settings.getFooterText());
		settingsRepo.save(st);
		AppConfig.reloadSettings = true;
		return "redirect:" + URL;
	}

	@RequestMapping(value="/carousel", method=RequestMethod.POST)
	public String saveCarousel(@Valid ImageUploadForm carouselForm) throws IOException {
		CarouselImages ci = carouselImagesRepo.findTopByOrderBySequenceDesc();
		carouselImagesRepo.save(CarouselImages.builder()
				.image(imageFileRepo.save(ImageFile.builder().image(carouselForm.getImageFile().getBytes()).build()))
				.sequence((ci == null ? 0 : ci.getSequence()) + 1).build());
		return "redirect:" + URL;
	}

	@RequestMapping(value="/carousel/sequence", method=RequestMethod.POST)
	@ResponseBody
	public boolean updateCarouselSequence(SequenceUpdater updater) {
		if (updater != null) {
			carouselImagesRepo.save(updater.getData().entrySet().stream().map(e -> {
				CarouselImages ci = carouselImagesRepo.findOne(e.getKey());
				ci.setSequence(e.getValue());
				return ci;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/carousel/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteCarouselImage(@PathVariable Long code) {
		carouselImagesRepo.delete(code);
		return true;
	}

	@RequestMapping(value="/gallery", method=RequestMethod.POST)
	public String saveGallery(@Valid ImageUploadForm galleryForm) throws IOException {
		GalleryImages gi = galleryImagesRepo.findTopByOrderBySequenceDesc();
		galleryImagesRepo.save(GalleryImages.builder()
				.image(imageFileRepo.save(ImageFile.builder().image(galleryForm.getImageFile().getBytes()).build()))
				.details(galleryForm.getDetails())
				.sequence((gi == null ? 0 : gi.getSequence()) + 1).build());
		return "redirect:" + URL;
	}

	@RequestMapping(value="/gallery/sequence", method=RequestMethod.POST)
	@ResponseBody
	public boolean updateGallerySequence(SequenceUpdater updater) {
		if (updater != null) {
			galleryImagesRepo.save(updater.getData().entrySet().stream().map(e -> {
				GalleryImages gi = galleryImagesRepo.findOne(e.getKey());
				gi.setSequence(e.getValue());
				return gi;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/gallery/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteGalleryImage(@PathVariable Long code) {
		galleryImagesRepo.delete(code);
		return true;
	}
}
