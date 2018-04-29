package com.niil.nogor.krishi;

import java.io.IOException;

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

	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired SaleTypeRepo saleTypeRepo;
	@Autowired CityRepo cityRepo;
	@Autowired AreaRepo areaRepo;
	@Autowired MaterialTypeRepo materialTypeRepo;
	@Autowired MaterialRepo materialRepo;

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
	}

	private byte[] getImageAsByte(String image) throws IOException {
		ClassPathResource backImgFile = new ClassPathResource(image);
		byte[] arrayPic = new byte[(int) backImgFile.contentLength()];
		backImgFile.getInputStream().read(arrayPic);
		return arrayPic;
	}

}
