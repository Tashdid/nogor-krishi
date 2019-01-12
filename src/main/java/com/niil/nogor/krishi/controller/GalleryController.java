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

import com.niil.nogor.krishi.entity.GalleryImages;
import com.niil.nogor.krishi.entity.GardenDesignImages;
import com.niil.nogor.krishi.entity.ImageFile;
import com.niil.nogor.krishi.repo.GalleryImagesRepo;
import com.niil.nogor.krishi.repo.GardenDesignImagesRepo;
import com.niil.nogor.krishi.repo.ImageFileRepo;
import com.niil.nogor.krishi.view.ImageUploadForm;
import com.niil.nogor.krishi.view.SequenceUpdater;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jan 12, 2019
 *
 */
@Controller
@RequestMapping("/settings")
public class GalleryController extends AbstractController {

	@Autowired ImageFileRepo imageFileRepo;
	@Autowired GalleryImagesRepo galleryImagesRepo;
	@Autowired GardenDesignImagesRepo gardenDesignImagesRepo;

	@RequestMapping(value = {"/gallery", "/gallery/", "/gallery/{gallery}"})
	public String gallery(@PathVariable(required=false) GalleryImages gallery, ModelMap model) {
		model.addAttribute("gallery", gallery == null ? new GalleryImages() : gallery);
		model.addAttribute("gbeans", galleryImagesRepo.findAll());
		return "gallery/gallery";
	}

	@RequestMapping(value="/gallery", method=RequestMethod.POST)
	public String saveGallery(@Valid ImageUploadForm galleryForm) throws IOException {
		GalleryImages gallery;
		if (galleryForm.getId() == null || (gallery = galleryImagesRepo.findOne(galleryForm.getId())) == null) {
			GalleryImages gi = galleryImagesRepo.findTopByOrderBySequenceDesc();
			int seq = (gi == null ? 0 : gi.getSequence()) + 1;
			gallery = GalleryImages.builder()
						.sequence(seq)
						.build();
		}
		gallery.setDetails(galleryForm.getDetails());
		if (galleryForm.getImageFile() != null && !galleryForm.getImageFile().isEmpty()) {
			ImageFile imf = gallery.getImage();
			if (imf == null) {
				imf = new ImageFile();
			}
			imf.setImage(galleryForm.getImageFile().getBytes());
			imageFileRepo.save(imf);
			gallery.setImage(imf);
		}
		gallery = galleryImagesRepo.save(gallery);
		return "redirect:/settings/gallery/" + gallery.getId();
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
		int i = 1;
		for(GalleryImages gi : galleryImagesRepo.findAllByOrderBySequenceAsc()) {
			gi.setSequence(i++);
			galleryImagesRepo.save(gi);
		}
		return true;
	}

	@RequestMapping(value = {"/gdesign", "/gdesign/", "/gdesign/{gdesign}"})
	public String gdesign(@PathVariable(required=false) GardenDesignImages gdesign, ModelMap model) {
		model.addAttribute("gdesign", gdesign == null ? new GardenDesignImages() : gdesign);
		model.addAttribute("gdbeans", gardenDesignImagesRepo.findAll());
		return "gallery/gdesign";
	}

	@RequestMapping(value="/gdesign", method=RequestMethod.POST)
	public String saveGardenDesign(@Valid ImageUploadForm galleryForm) throws IOException {
		GardenDesignImages gdesign;
		if (galleryForm.getId() == null || (gdesign = gardenDesignImagesRepo.findOne(galleryForm.getId())) == null) {
			GardenDesignImages gd = gardenDesignImagesRepo.findTopByOrderBySequenceDesc();
			int seq = (gd == null ? 0 : gd.getSequence()) + 1;
			gdesign = GardenDesignImages.builder()
						.sequence(seq)
						.build();
		}
		gdesign.setDetails(galleryForm.getDetails());
		if (galleryForm.getImageFile() != null && !galleryForm.getImageFile().isEmpty()) {
			ImageFile imf = gdesign.getImage();
			if (imf == null) {
				imf = new ImageFile();
			}
			imf.setImage(galleryForm.getImageFile().getBytes());
			imageFileRepo.save(imf);
			gdesign.setImage(imf);
		}
		gdesign = gardenDesignImagesRepo.save(gdesign);
		return "redirect:/settings/gdesign/" + gdesign.getId();
	}

	@RequestMapping(value="/gdesign/sequence", method=RequestMethod.POST)
	@ResponseBody
	public boolean updateGardenDesignSequence(SequenceUpdater updater) {
		if (updater != null) {
			gardenDesignImagesRepo.save(updater.getData().entrySet().stream().map(e -> {
				GardenDesignImages gdi = gardenDesignImagesRepo.findOne(e.getKey());
				gdi.setSequence(e.getValue());
				return gdi;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/gdesign/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteGardenDesignImage(@PathVariable Long code) {
		gardenDesignImagesRepo.delete(code);
		int i = 1;
		for(GardenDesignImages gdi : gardenDesignImagesRepo.findAllByOrderBySequenceAsc()) {
			gdi.setSequence(i++);
			gardenDesignImagesRepo.save(gdi);
		}
		return true;
	}
}
