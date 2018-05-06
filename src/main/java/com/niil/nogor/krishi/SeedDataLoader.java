package com.niil.nogor.krishi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Component
@Profile("dev")
public class SeedDataLoader implements ApplicationRunner {

	@Autowired SaleTypeRepo saleTypeRepo;
	@Autowired CityRepo cityRepo;
	@Autowired AreaRepo areaRepo;
	@Autowired MaterialTypeRepo materialTypeRepo;
	@Autowired MaterialRepo materialRepo;
	@Autowired NurseryTypeRepo nurseryTypeRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired ProductRepo productRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;

	@SuppressWarnings("unused")
	public void run(ApplicationArguments args) throws IOException {

		// Sale Type Entry
		SaleType chara = saleTypeRepo.save(SaleType.builder().name("চারা").build());
		SaleType bij = saleTypeRepo.save(SaleType.builder().name("বীজ").build());
		
		// City Entry
		City dhaka = cityRepo.save(City.builder().name("ঢাকা").sequence(1).build());

		// Area Entry
		Area uttara = areaRepo.save(Area.builder().city(dhaka).name("উত্তরা")
				.latitude(23.873751).longitude(90.396454).sequence(1).build());
		Area dhanmondi = areaRepo.save(Area.builder().city(dhaka).name("ধানমন্ডি")
				.latitude(23.746466).longitude(90.376015).sequence(2).build());

		MaterialType patroT = materialTypeRepo.save(MaterialType.builder().name("পাত্র").sequence(1).build());
		MaterialType matiT = materialTypeRepo.save(MaterialType.builder().name("মাটি").sequence(2).build());

		Material tob = materialRepo.save(Material.builder().name("টব").type(patroT).build());
		Material drum = materialRepo.save(Material.builder().name("ড্রাম").type(patroT).build());
		Material mati = materialRepo.save(Material.builder().name("মাটি").type(matiT).build());
		
		ProductType shak = productTypeRepo.save(ProductType.builder().name("শাক")
				.alternativeName("Leafy Vegetables").sequence(1)
				.image(getImageAsByte("seed/shak.png"))
				.icon(getImageAsByte("seed/icon-shak.jpg")).build());
		ProductType shobji = productTypeRepo.save(ProductType.builder().name("সবজি")
				.alternativeName("Vegetables").sequence(2)
				.image(getImageAsByte("seed/vegetables.png"))
				.icon(getImageAsByte("seed/icon-shobji.jpg")).build());
		ProductType fruit = productTypeRepo.save(ProductType.builder().name("ফলমূল")
				.alternativeName("Fruits").sequence(3)
				.image(getImageAsByte("seed/fruits.png"))
				.icon(getImageAsByte("seed/icon-fruit.jpg")).build());
		ProductType oishodhi = productTypeRepo.save(ProductType.builder().name("ঔষধি ও রান্নার উপকরণ")
				.alternativeName("Herbs & Ingredients").sequence(4)
				.image(getImageAsByte("seed/herbs.png"))
				.icon(getImageAsByte("seed/icon-herbs.jpg")).build());
		ProductType deco = productTypeRepo.save(ProductType.builder().name("সৌন্দর্য বর্ধন (ছোট ছাদ)")
				.alternativeName("Decorative (Small Rooftops)").sequence(5)
				.image(getImageAsByte("seed/decorative.png"))
				.icon(getImageAsByte("seed/icon-decorative.jpg")).build());
		ProductType corner = productTypeRepo.save(ProductType.builder().name("কর্নার এবং ইনডোর")
				.alternativeName("Corners & Indoors").sequence(6)
				.image(getImageAsByte("seed/corners.png"))
				.icon(getImageAsByte("seed/icon-corners.jpg")).build());

		Product lalshak = productRepo.save(Product.builder().name("লাল শাক")
				.alternativeName("Red Leaf").sequence(1)
				.description("জনপ্রিয় শাকের মধ্যে লাল শাক অন্যতম। খেতেও যেমন সুস্বাদু তেমনি পুষ্টিগুণে ভরপুর। পুষ্টিগুণ এবং রোগ প্রতিরোধের দিক দিয়ে লাল শাকের চেয়ে যোজন যোজন দূরে অন্যান্য শাক।\n\nলাল শাক রূপে যেমন মনোহারী গুণেও তেমন কার্যকরী। পাতের ভাতকে নতুন রূপ দিতেও সেরা। ছোট বড় সবাই এর স্বাদের ভক্ত।\n\nলাল শাক, ডাঁটা শাক বা ডাঙ্গা শাক, এরা সবই একই গোত্রের এবং এদের প্রজাতিও একই। বিশ্বের অন্য দেশে এদেরকে ফুলের গাছ হিসাবে ব্যবহার করা হলেও আমাদের বাংলাদেশে কিন্তু আমরা একে সবজি হিসাবে ব্যবহার করি।")
				.image(getImageAsByte("seed/tree-lal-shak.png"))
				.productivity("২ কেজি প্রতি মাস")
				.benefits("ভিটামিন এ, বি ও কে দিয়ে ভরপুর, নতুন রক্ত হতে সাহায্যয় করে, ইমিউনিটি বাড়ায়।")
				.icon(getImageAsByte("seed/icon-herbs.jpg"))
				.type(shak).saleType(chara).materials(Arrays.asList(tob, mati)).build());
		Product mulashak = productRepo.save(Product.builder().name("মুলা শাক")
				.alternativeName("Mula Shak").sequence(2)
				.description("জনপ্রিয় শাকের মধ্যে মুলা শাক অন্যতম। খেতেও যেমন সুস্বাদু তেমনি পুষ্টিগুণে ভরপুর। পুষ্টিগুণ এবং রোগ প্রতিরোধের দিক দিয়ে লাল শাকের চেয়ে যোজন যোজন দূরে অন্যান্য শাক।\n\nলাল শাক রূপে যেমন মনোহারী গুণেও তেমন কার্যকরী। পাতের ভাতকে নতুন রূপ দিতেও সেরা। ছোট বড় সবাই এর স্বাদের ভক্ত।\n\nলাল শাক, ডাঁটা শাক বা ডাঙ্গা শাক, এরা সবই একই গোত্রের এবং এদের প্রজাতিও একই। বিশ্বের অন্য দেশে এদেরকে ফুলের গাছ হিসাবে ব্যবহার করা হলেও আমাদের বাংলাদেশে কিন্তু আমরা একে সবজি হিসাবে ব্যবহার করি।")
				.image(getImageAsByte("seed/tree-kochu.png"))
				.productivity("১ কেজি প্রতি মাস")
				.benefits("এই শাকটিকে অ্যান্টিক্যান্সার উপাদান সমৃদ্ধ সবজির তালিকায় ব্রোকলি এবং কপির পাশাপাশি অবস্থান দেওয়া যেতে পারে। মুলা শাক গ্রহণ করলে অ্যালার্জি ও হৃদপিণ্ডের বিভিন্ন রোগ থেকে সুরক্ষা পাওয়া সম্ভব।")
				.icon(getImageAsByte("seed/icon-herbs.jpg"))
				.type(shak).saleType(bij).materials(Arrays.asList(drum, mati)).build());

		NurseryType gov = nurseryTypeRepo.save(NurseryType.builder().name("সরকারী")
				.image(getImageAsByte("seed/nursery-logo-govt.jpg"))
				.sequence(1).build());
		NurseryType priv = nurseryTypeRepo.save(NurseryType.builder().name("বেসরকারী")
				.image(getImageAsByte("seed/nursery-logo-private.png"))
				.sequence(2).build());

		Nursery s1 = nurseryRepo.save(Nursery.builder().name("Test Nursery")
				.type(gov).area(uttara).address("Sector 11")
				.phone("123456578").sequence(1)
				.latitude(23.873751).longitude(90.396454).build());
		Nursery s2 = nurseryRepo.save(Nursery.builder().name("Test Nursery 2")
				.type(gov).area(uttara).address("Sector 12")
				.phone("123456578").sequence(2)
				.latitude(23.746466).longitude(90.376015).build());

		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s1).price(new BigDecimal("80")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s1).price(new BigDecimal("200")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s1).price(new BigDecimal("50")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s2).price(new BigDecimal("70")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s2).price(new BigDecimal("180")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s2).price(new BigDecimal("40")).build());

		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s1).price(new BigDecimal("20")).build());
		productPriceRepo.save(ProductPrice.builder().product(mulashak).nursery(s1).price(new BigDecimal("20")).build());
		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s2).price(new BigDecimal("25")).build());
		productPriceRepo.save(ProductPrice.builder().product(mulashak).nursery(s2).price(new BigDecimal("25")).build());
	}

	private byte[] getImageAsByte(String image) throws IOException {
		ClassPathResource backImgFile = new ClassPathResource(image);
		byte[] arrayPic = new byte[(int) backImgFile.contentLength()];
		backImgFile.getInputStream().read(arrayPic);
		return arrayPic;
	}

}
