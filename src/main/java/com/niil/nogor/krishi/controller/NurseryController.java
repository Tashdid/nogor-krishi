package com.niil.nogor.krishi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;
import com.niil.nogor.krishi.view.SequenceUpdater;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Slf4j
@Controller
@RequestMapping(NurseryController.URL)
public class NurseryController extends AbstractController {
	static final String URL = "/manage/nursery";

	@Autowired AreaRepo areaRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired NurseryTypeRepo nurseryTypeRepo;
	@Autowired MaterialRepo materialRepo;
	@Autowired ProductRepo productRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired SaleTypeRepo saleTypeRepo;

	@RequestMapping
	public String newScreen(ModelMap model) {
		return updateScreen(null, model);
	}

	@RequestMapping(value="/{code}")
	public String updateScreen(@PathVariable Long code, final ModelMap model) {
		model.addAttribute("basePath", URL + "/");
		Nursery bean = code == null ? new Nursery() : nurseryRepo.findOne(code);
		if (bean == null) bean = new Nursery();
		model.addAttribute("bean", bean);
		model.addAttribute("brand", "Nursery");
		model.addAttribute("allbeans", nurseryRepo.findAll());
		model.addAttribute("types", nurseryTypeRepo.findAllByOrderBySequenceAsc());
		model.addAttribute("areas", areaRepo.findAll());
		if (bean.getId() != null) {
			loadNurseryPrices(bean, model);
		}
		return URL.substring(1);
	}

	public void loadNurseryPrices(Nursery bean, final ModelMap model) {
		List<MaterialPrice> mplist = materialPriceRepo.findAllByNursery(bean);
		model.addAttribute("materialprices", mplist);
		model.addAttribute("materials", materialRepo.findAll().stream()
				.filter(m -> mplist.stream().noneMatch(mm -> mm.getMaterial().getId().equals(m.getId())))
				.collect(Collectors.toList()));
		List<ProductPrice> pplist = productPriceRepo.findAllByNursery(bean);
		model.addAttribute("productprices", pplist);
		List<SaleType> sTypes = saleTypeRepo.findAll();
		model.addAttribute("stypes", sTypes);
		List<Product> prods = productRepo.findAllByOrderByTypeSequenceAsc().stream()
				.filter(p -> pplist.stream().filter(pp -> pp.getProduct().getId().equals(p.getId())).count() < sTypes.size())
			.collect(Collectors.toList());
		model.addAttribute("prods", prods);
		model.addAttribute("prodAvSTypes", prods.stream()
				.filter(p -> pplist.stream().filter(pp -> pp.getProduct().getId().equals(p.getId())).count() < sTypes.size())
			.collect(Collectors.toMap(p -> p.getId(), p -> {
					List<Long> pprs = pplist.stream()
							.filter(pp -> pp.getProduct().getId().equals(p.getId())).map(pp -> pp.getSaleType().getId()).collect(Collectors.toList());
					return sTypes.stream().filter(s -> !pprs.contains(s.getId())).map(s -> s.getId().toString()).collect(Collectors.joining(","));
		})));
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Nursery type) throws IOException {
		Nursery bean;
		if (type.getId() == null || (bean = nurseryRepo.findOne(type.getId())) == null) {
			Nursery lt = nurseryRepo.findTopByAreaOrderBySequenceDesc(type.getArea());
			int seq = (lt == null ? 0 : lt.getSequence()) + 1;
			bean = Nursery.builder()
						.sequence(seq)
						.build();
		}
		bean.setName(type.getName());
		bean.setAddress(type.getAddress());
		bean.setPhone(type.getPhone());
		bean.setArea(type.getArea());
		bean.setType(type.getType());
		bean.setLatitude(type.getLatitude());
		bean.setLongitude(type.getLongitude());
		bean = nurseryRepo.save(bean);
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/table", method=RequestMethod.POST)
	@ResponseBody
	public boolean tableDataPost(SequenceUpdater updater) {
		if (updater != null) {
			nurseryRepo.save(updater.getData().entrySet().stream().map(e -> {
				Nursery bean = nurseryRepo.findOne(e.getKey());
				bean.setSequence(e.getValue());
				return bean;
			}).collect(Collectors.toList()));
		}
		return true;
	}

	@RequestMapping(value="/{code}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean delete(@PathVariable Long code) {
		nurseryRepo.delete(code);
		return true;
	}

	@RequestMapping(value="/{code}/product", method=RequestMethod.POST)
	public String addProductPrice(@PathVariable Long code, ProductPrice mprice, RedirectAttributes redirectAttrs) {
		Nursery bean = nurseryRepo.findOne(code);
		mprice.setNursery(bean);
		productPriceRepo.save(mprice);
		redirectAttrs.addFlashAttribute("clickonload", "list");
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/{code}/product/{product}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteProductPrice(@PathVariable Long code, @PathVariable Long product) {
		Nursery bean = nurseryRepo.findOne(code);
		ProductPrice pp = productPriceRepo.findOne(product);
		if (pp != null && pp.getNursery().getId().equals(bean.getId())) {
			productPriceRepo.delete(pp);
		}
		return true;
	}

	@RequestMapping(value="/{code}/material", method=RequestMethod.POST)
	public String addMaterialPrice(@PathVariable Long code, MaterialPrice mprice, RedirectAttributes redirectAttrs) {
		Nursery bean = nurseryRepo.findOne(code);
		mprice.setNursery(bean);
		materialPriceRepo.save(mprice);
		redirectAttrs.addFlashAttribute("clickonload", "list");
		return "redirect:" + URL + "/" + bean.getId();
	}

	@RequestMapping(value="/{code}/material/{material}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteMaterialPrice(@PathVariable Long code, @PathVariable Long material) {
		Nursery bean = nurseryRepo.findOne(code);
		MaterialPrice mtp = materialPriceRepo.findOne(material);
		if (mtp != null && mtp.getNursery().getId().equals(bean.getId())) {
			materialPriceRepo.delete(mtp);
		}
		return true;
	}

	@RequestMapping(value="/{nursery}/product/price", method=RequestMethod.GET)
	public ResponseEntity<byte[]> exportProductPrice(@PathVariable Nursery nursery) {
		StringBuilder rsp = new StringBuilder("কোড,বিক্রয় টাইপ কোড,প্রকার,নাম,বিক্রয় টাইপ,মূল্য\n");
		List<ProductPrice> prices = productPriceRepo.findAllByNursery(nursery);
		List<SaleType> sTypes = saleTypeRepo.findAll();
		rsp.append(productRepo.findAllByOrderByTypeSequenceAsc().stream().map(p -> {
			return sTypes.stream().map(s -> {
				ProductPrice pr = prices.stream().filter(pp -> pp.getProduct().getId().equals(p.getId()) && pp.getSaleType().getId().equals(s.getId())).findFirst().orElse(null);
				return Arrays.asList(p.getId().toString(), s.getId().toString(), p.getType().getName(), p.getName(), s.getName(), (pr == null || pr.getPrice() == null ? "" : pr.getPrice().toPlainString())).stream().collect(Collectors.joining(","));
			}).collect(Collectors.joining("\n"));
		}).collect(Collectors.joining("\n")));
		return prepareResponse(rsp.toString().getBytes(), "product_prices.csv");
	}

	@RequestMapping(value="/{nursery}/material/price", method=RequestMethod.GET)
	public ResponseEntity<byte[]> exportMaterialPrice(@PathVariable Nursery nursery) {
		StringBuilder rsp = new StringBuilder("কোড,প্রকার,নাম,মূল্য\n");
		List<MaterialPrice> prices = materialPriceRepo.findAllByNursery(nursery);
		rsp.append(materialRepo.findAll().stream().map(m -> {
			MaterialPrice mr = prices.stream().filter(mp -> mp.getMaterial().getId().equals(m.getId())).findFirst().orElse(null);
			return Arrays.asList(m.getId().toString(), m.getType().getName(), m.getName(), (mr == null || mr.getPrice() == null ? "" : mr.getPrice().toPlainString())).stream().collect(Collectors.joining(","));
		}).collect(Collectors.joining("\n")));
		return prepareResponse(rsp.toString().getBytes(), "material_prices.csv");
	}

	private ResponseEntity<byte[]> prepareResponse(byte[] output, String filename) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("charset", "utf-8");
		responseHeaders.setContentType(MediaType.valueOf("application/csv"));
		responseHeaders.setContentLength(output.length);
		responseHeaders.set("Content-disposition", "attachment; filename=" + filename);
		return new ResponseEntity<byte[]>(output, responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value="/{nursery}/product/price", method=RequestMethod.POST)
	public String importProductPrice(@PathVariable Nursery nursery, MultipartFile file, RedirectAttributes redirectAttrs) {
		List<ProductPrice> prices = productPriceRepo.findAllByNursery(nursery);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line = reader.readLine();
			if (line != null) {
				while((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					if (parts.length < 6 || !NumberUtils.isNumber(parts[0])
							|| !NumberUtils.isNumber(parts[1])
							|| !NumberUtils.isNumber(parts[5])) continue;
					Product pr = productRepo.findOne(Long.valueOf(parts[0]));
					if (pr == null) continue;
					SaleType st = saleTypeRepo.findOne(Long.valueOf(parts[1]));
					if (st == null) continue;
					ProductPrice ppr = ProductPrice
							.builder()
							.id(prices.stream().filter(pp -> pp.getProduct().getId().equals(pr.getId()) && pp.getSaleType().getId().equals(st.getId())).findFirst().orElse(new ProductPrice()).getId())
							.nursery(nursery).product(pr)
							.saleType(st)
							.price(new BigDecimal(parts[5]))
							.build();
					productPriceRepo.save(ppr);
				}
			}
		} catch (IOException e) {
			log.error("Failed to upload product price", e);
		}
		
		redirectAttrs.addFlashAttribute("clickonload", "list");
		return "redirect:" + URL + "/" + nursery.getId();
	}

	@RequestMapping(value="/{nursery}/material/price", method=RequestMethod.POST)
	public String importMaterialPrice(@PathVariable Nursery nursery, MultipartFile file, RedirectAttributes redirectAttrs) {
		List<MaterialPrice> prices = materialPriceRepo.findAllByNursery(nursery);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line = reader.readLine();
			if (line != null) {
				while((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					if (parts.length < 4 || !NumberUtils.isNumber(parts[0])
							|| !NumberUtils.isNumber(parts[3])) continue;
					Material mr = materialRepo.findOne(Long.valueOf(parts[0]));
					if (mr == null) continue;
					MaterialPrice mpr = MaterialPrice
							.builder()
							.id(prices.stream().filter(pp -> pp.getMaterial().getId().equals(mr.getId())).findFirst().orElse(new MaterialPrice()).getId())
							.nursery(nursery)
							.material(mr)
							.price(new BigDecimal(parts[3]))
							.build();
					materialPriceRepo.save(mpr);
				}
			}
		} catch (IOException e) {
			log.error("Failed to upload material price", e);
		}
		redirectAttrs.addFlashAttribute("clickonload", "list");
		return "redirect:" + URL + "/" + nursery.getId();
	}
}


