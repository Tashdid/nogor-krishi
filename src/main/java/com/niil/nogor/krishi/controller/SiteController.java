package com.niil.nogor.krishi.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.config.Constants;
import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;
import com.niil.nogor.krishi.service.SecurityService;
import com.niil.nogor.krishi.view.LArea;
import com.niil.nogor.krishi.view.LNursery;
import com.niil.nogor.krishi.view.LProduct;
import com.niil.nogor.krishi.view.LayoutRQ;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 29, 2018
 *
 */
@Controller
@RequestMapping
public class SiteController extends AbstractController {
	private static final String ERROR_PAGE = "error";
	private static final String LAYOUT_NOT_FOUND = "দুঃখিত, বাগান লে-আউট পাওয়া যায় নাই!";

	private Comparator<Nursery> byNurseryType = Comparator.comparing(
			parent -> parent.getType().getSequence()
	);

	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired ProductRepo productRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired AreaRepo areaRepo;
	@Autowired GardenLayoutRepo gardenLayoutRepo;
	@Autowired GardenBlockRepo gardenBlockRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired SecurityService securityService;
	@Autowired GalleryImagesRepo galleryImagesRepo;
	@Autowired CarouselImagesRepo carouselImagesRepo;
	@Autowired GardenDesignImagesRepo gardenDesignImagesRepo;
	@Autowired MaterialRepo materialRepo;

	@RequestMapping
	public String homePage(final ModelMap model) {
		List<ProductType> types = productTypeRepo.findAllByOrderBySequenceAsc();
		model.addAttribute("types", types);
		List<Area> areas = areaRepo.findAll();
		model.addAttribute("areas", areas);
		model.addAttribute("cbeans", carouselImagesRepo.findAllByOrderBySequenceAsc());
		return "site/index";
	}

	@RequestMapping({"/products", "/products/{type}"})
	public String allProducts(@PathVariable(required=false) ProductType type,
			@RequestParam(required=false) Product product,
			@RequestParam(required=false) Material material,
			final ModelMap model) {
		model.addAttribute("type", type);
		model.addAttribute("types", productTypeRepo.findAllByOrderBySequenceAsc());
		if (type != null && type.isLinkedToMaterial()) {
			if (material == null) {
				model.addAttribute("products", materialRepo.findAllByOrderBySequenceAsc());
			} else {
				model.addAttribute("product", material);
				List<MaterialPrice> prices = materialPriceRepo.findAllByMaterial(material);
				List<Nursery> nurseries = prices.stream().map(p -> p.getNursery()).distinct()
						.sorted(byNurseryType.thenComparing(Nursery::getSequence))
						.collect(Collectors.toList());
				model.addAttribute("nurseries", nurseries);
				model.addAttribute("areas", nurseries.stream().map(n -> n.getArea()).distinct()
						.sorted(Comparator.comparing(Area::getSequence))
						.collect(Collectors.toList()));
				model.addAttribute("nextProduct", materialRepo.findTopBySequenceGreaterThanOrderBySequence(material.getSequence()));
				model.addAttribute("previousProduct", materialRepo.findTopBySequenceLessThanOrderBySequenceDesc(material.getSequence()));
			}
		} else if (product != null) {
			model.addAttribute("type", product.getType());
			model.addAttribute("product", product);
			List<ProductPrice> prices = productPriceRepo.findAllByProduct(product);
			List<Nursery> nurseries = prices.stream().map(p -> p.getNursery()).distinct()
					.sorted(byNurseryType.thenComparing(Nursery::getSequence))
					.collect(Collectors.toList());
			model.addAttribute("nurseries", nurseries);
			model.addAttribute("areas", nurseries.stream().map(n -> n.getArea()).distinct()
					.sorted(Comparator.comparing(Area::getSequence))
					.collect(Collectors.toList()));
			model.addAttribute("nextProduct", productRepo.findTopByTypeAndSequenceGreaterThanOrderBySequence(product.getType(), product.getSequence()));
			model.addAttribute("previousProduct", productRepo.findTopByTypeAndSequenceLessThanOrderBySequenceDesc(product.getType(), product.getSequence()));
		} else {
			model.addAttribute("products", type == null ? cacheRepo.getAllProducts() : cacheRepo.getProducts(type));
		}
		return "site/ponno";
	}

	@RequestMapping("/layout")
	public String layout(final ModelMap model) {
		List<ProductType> types = productTypeRepo.findAllByOrderBySequenceAsc();
		model.addAttribute("types", types.stream().collect(Collectors.toMap(t -> t.getId(), t -> t.getName())));
		if (!types.isEmpty()) {
			typeProducts(types.get(0), model);
		}
		return "site/layout";
	}

	@ResponseBody
	@RequestMapping("/nurseries")
	public Map<Object, List<Nursery>> nurseries() {
		Map<Object, List<Nursery>> nurseries = nurseryRepo.findAll().stream().collect(Collectors.groupingBy(n -> n.getArea().getId(), Collectors.toList()));
		return nurseries;
	}

	@RequestMapping("/layout/items/{type}")
	public String typeProducts(@PathVariable ProductType type, final ModelMap model) {
		List<Product> list = productRepo.findAllByTypeOrderBySequenceAsc(type);
		list.forEach(p -> p.setImage(null));
		model.addAttribute("products", list);
		return "site/layout :: products";
	}

	@RequestMapping(value="/layout", method=RequestMethod.POST)
	public String layoutSave(@Valid LayoutRQ layout, final ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
			final RedirectAttributes redirectAttrs) {
		GardenLayout gl = new GardenLayout();
		gl.setLength(layout.getLength());
		gl.setWidth(layout.getWidth());
		gl.setImage(Base64.getDecoder().decode(layout.getEncodedImage()));
		GardenLayout sgl = gardenLayoutRepo.save(gl);
		layout.getBlocks().forEach(bl -> {
			bl.setGardenLayout(sgl);
			gardenBlockRepo.save(bl);
		});

		User user = securityService.findLoggedInUser();
		if (user == null) {
			request.getSession().setAttribute(Constants.NEWLY_CREATED_GARDEN_ID, sgl.getId());
		} else {
			sgl.setUser(user);
			gardenLayoutRepo.save(sgl);
		}
		redirectAttrs.addFlashAttribute("newlayout", true);
		return "redirect:/exlayout/" + sgl.getId();
	}

	@RequestMapping(value="/exlayout/{layout}")
	public String showLayout(@PathVariable(required=false) GardenLayout layout,
			HttpServletRequest request, HttpServletResponse response, final ModelMap model) {
		if (layout == null) {
			model.addAttribute("msg", LAYOUT_NOT_FOUND);
			return ERROR_PAGE;
		}
		User user = securityService.findLoggedInUser();
		Object ncgi = request.getSession().getAttribute(Constants.NEWLY_CREATED_GARDEN_ID);
		if (ncgi != null) {
			GardenLayout gl = gardenLayoutRepo.getOne((Long) ncgi);
			gl.setUser(user);
			gl = gardenLayoutRepo.save(gl);
			request.getSession().removeAttribute(Constants.NEWLY_CREATED_GARDEN_ID);
			if (layout.getId().equals(gl.getId())) {
				layout = gl;
				model.addAttribute("newlayout", true);
			}
		}
		model.addAttribute("layout", layout);

		if (layout.getUser() == null || user == null || !user.getId().equals(layout.getUser().getId())) {
			model.addAttribute("msg", LAYOUT_NOT_FOUND);
			return ERROR_PAGE;
		}

		List<Product> prods = gardenBlockRepo.findAllByGardenLayout(layout).stream().map(b -> b.getProduct()).distinct().collect(Collectors.toList());
		List<LProduct> lprods = prods.stream().map(p -> {
			LProduct lp = new LProduct();
			lp.setProduct(p);
			List<ProductPrice> pPrices = productPriceRepo.findAllByProduct(p);
			lp.setAreas(pPrices.stream().collect(Collectors.groupingBy(pp -> pp.getNursery().getArea())).entrySet().stream().map(e -> {
				LArea lar = new LArea();
				lar.setArea(e.getKey());
				lar.setNurseries(e.getValue().stream().collect(Collectors.groupingBy(pp -> pp.getNursery())).entrySet().stream().map(e1 -> {
					LNursery ln = new LNursery();
					ln.setNursery(e1.getKey());
					ln.setProductPrices(e1.getValue());
					ln.setMaterialPrices(materialPriceRepo.findAllByNursery(e1.getKey()).stream().collect(Collectors.groupingBy(mp -> mp.getMaterial().getType().getName())));
					return ln;
				}).collect(Collectors.toList()));
				return lar;
			}).collect(Collectors.toList()));
			return lp;
		}).collect(Collectors.toList());
		model.addAttribute("lprods", lprods);
		model.addAttribute("nurseries", nurseries());
		return "site/exlayout";
	}

	@RequestMapping(value="/exlayout/list")
	public String myLayouts(final ModelMap model) {
		User user = securityService.findLoggedInUser();
		model.addAttribute("layouts", user == null ? Collections.emptyList() : gardenLayoutRepo.findByUserOrderByCreatedOnDesc(user));
		return "site/exlayoutlist";
	}

	@ResponseBody
	@RequestMapping(value="/exlayout/{layout}", method=RequestMethod.POST)
	public Object deleteLayout(@PathVariable(required=false) GardenLayout layout) {
		if (layout == null || !securityService.findLoggedInUser().getId().equals(layout.getUser().getId()))
			return LAYOUT_NOT_FOUND;

		gardenLayoutRepo.delete(layout);
		return true;
	}

	@RequestMapping("/gallery")
	public String galleryPage(ModelMap model) {
		model.addAttribute("gbeans", galleryImagesRepo.findAllByOrderBySequenceAsc());
		model.addAttribute("pageTitle", getTitle(siteSettings.getPhotoGalleryTitle(), "ফটো গ্যালারি"));
		return "site/gallery";
	}

	private String getTitle(String title, String defaultTitle) {
		return StringUtils.isEmpty(title) ? defaultTitle : title;
	}

	@RequestMapping("/gdesign")
	public String gardenDesignPage(ModelMap model) {
		model.addAttribute("gbeans", gardenDesignImagesRepo.findAllByOrderBySequenceAsc());
		model.addAttribute("pageTitle", getTitle(siteSettings.getGardenDesignTitle(), "বাগানের সহায়ক নকশা"));
		return "site/gallery";
	}
}
