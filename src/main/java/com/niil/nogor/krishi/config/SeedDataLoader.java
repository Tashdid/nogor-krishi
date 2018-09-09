package com.niil.nogor.krishi.config;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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

		if (!saleTypeRepo.findAll().isEmpty()) return;
		// Sale Type Entry
		SaleType chara = saleTypeRepo.save(SaleType.builder().name("চারা").build());
		SaleType bij = saleTypeRepo.save(SaleType.builder().name("বীজ").build());
		
		// City Entry
		City dhaka = cityRepo.save(City.builder().name("ঢাকা").sequence(1).build());

		// Area Entry
		Area mirpur = areaRepo.save(Area.builder().city(dhaka).name("মিরপুর")
				.latitude(23.8069355).longitude(90.3665198).sequence(1).build());
		Area dhanmondi = areaRepo.save(Area.builder().city(dhaka).name("ধানমন্ডি")
				.latitude(23.746466).longitude(90.376015).sequence(2).build());
		Area uttara = areaRepo.save(Area.builder().city(dhaka).name("উত্তরা")
				.latitude(23.873751).longitude(90.396454).sequence(3).build());

		MaterialType patroT = materialTypeRepo.save(MaterialType.builder().name("পাত্র").sequence(1).build());
		MaterialType matiT = materialTypeRepo.save(MaterialType.builder().name("মাটি").sequence(2).build());

		Material tob = materialRepo.save(Material.builder().name("টব").type(patroT).build());
		Material drum = materialRepo.save(Material.builder().name("ড্রাম").type(patroT).build());
		Material mati = materialRepo.save(Material.builder().name("মাটি").type(matiT).build());
		
		ProductType shak = productTypeRepo.save(ProductType.builder().name("শাক")
				.alternativeName("Leafy Vegetables").sequence(1)
				.image(getImageAsByte("seed/shak.png"))
				.icon(getImageAsByte("seed/icon-product-shak.png")).build());
		ProductType shobji = productTypeRepo.save(ProductType.builder().name("সবজি")
				.alternativeName("Vegetables").sequence(2)
				.image(getImageAsByte("seed/vegetables.png"))
				.icon(getImageAsByte("seed/icon-product-shobji.png")).build());
		ProductType fruit = productTypeRepo.save(ProductType.builder().name("ফলমূল")
				.alternativeName("Fruits").sequence(3)
				.image(getImageAsByte("seed/fruits.png"))
				.icon(getImageAsByte("seed/icon-product-fruit.png")).build());
		ProductType oishodhi = productTypeRepo.save(ProductType.builder().name("ঔষধি ও রান্নার উপকরণ")
				.alternativeName("Herbs & Ingredients").sequence(4)
				.image(getImageAsByte("seed/herbs.png"))
				.icon(getImageAsByte("seed/icon-product-oushodhi.png")).build());
		ProductType deco = productTypeRepo.save(ProductType.builder().name("সৌন্দর্য বর্ধন (ছোট ছাদ)")
				.alternativeName("Decorative (Small Rooftops)").sequence(5)
				.image(getImageAsByte("seed/decorative.png"))
				.icon(getImageAsByte("seed/icon-product-shak.png")).build());
		ProductType corner = productTypeRepo.save(ProductType.builder().name("কর্নার এবং ইনডোর")
				.alternativeName("Corners & Indoors").sequence(6)
				.image(getImageAsByte("seed/corners.png"))
				.icon(getImageAsByte("seed/icon-product-shak.png")).build());

		Product lalshak = productRepo.save(Product.builder().name("লাল শাক")
				.alternativeName("Red Leaf").sequence(1)
				.description("জনপ্রিয় শাকের মধ্যে লাল শাক অন্যতম। খেতেও যেমন সুস্বাদু তেমনি পুষ্টিগুণে ভরপুর। পুষ্টিগুণ এবং রোগ প্রতিরোধের দিক দিয়ে লাল শাকের চেয়ে যোজন যোজন দূরে অন্যান্য শাক।\n\nলাল শাক রূপে যেমন মনোহারী গুণেও তেমন কার্যকরী। পাতের ভাতকে নতুন রূপ দিতেও সেরা। ছোট বড় সবাই এর স্বাদের ভক্ত।\n\nলাল শাক, ডাঁটা শাক বা ডাঙ্গা শাক, এরা সবই একই গোত্রের এবং এদের প্রজাতিও একই। বিশ্বের অন্য দেশে এদেরকে ফুলের গাছ হিসাবে ব্যবহার করা হলেও আমাদের বাংলাদেশে কিন্তু আমরা একে সবজি হিসাবে ব্যবহার করি।")
				.image(getImageAsByte("seed/tree-lal-shak.png"))
				.productivity("২ কেজি প্রতি মাস")
				.benefits("ভিটামিন এ, বি ও কে দিয়ে ভরপুর, নতুন রক্ত হতে সাহায্যয় করে, ইমিউনিটি বাড়ায়।")
				.icon(getImageAsByte("seed/icon-herbs.jpg"))
				.type(shak).build());
		Product kochushak = productRepo.save(Product.builder().name("কচু")
				.alternativeName("Kuchu").sequence(3)
				.description("কচু Araceae গোত্রভূক্ত একধরনের কন্দ জাতীয় ফসল। কচু মানুষের দ্বারা চাষকৃত প্রাচীন উদ্ভিদগুলোর মধ্যে একটি। বাংলাদেশ ও পশ্চিমবঙ্গের প্রায় সব এলাকায় কম বেশি কচু দেখতে পাওয়া যায়। রাস্তার পাশে, বাড়ির আনাচে কানাচে, বিভিন্ন পতিত জমিতে অনাদরে-অবহেলায় অনেক সময় কচু হয়ে থাকতে দেখা যায়। বহু জাতের কচু রয়েছে। কিছু কিছু জাতের কচু রীতিমত যত্নের সাথে চাষ করতে হয়।\n\nবনে জঙ্গলে যেসব কচু আপনাআপনি জন্মায় সেগুলকে সাধারণত বুনো কচু বলা হয়। এধরনের কচুর অনেকগুলো জাত মানুষের খাবারের উপযোগী নয়। খাবার উপযোগী জাতগুলোর অন্যতম হচ্ছে মুখীকচু, পানিকচু, পঞ্চমুখী কচু, পাইদনাইল, ওলকচু, দুধকচু, মানকচু, শোলাকচু ইত্যাদি। সবজি হিসেবে ব্যবহার ছাড়াও সৌন্দর্যের কারণে কিছু কিছু প্রজাতির কচু টবে ও বাগানে চাষ করা হয়। এদের মধ্যে কতগুলোর রয়েছে বেশ বাহারী পাতা, আবার কতগুলোর রয়েছে অত্যন্ত সুন্দর ফুল।\n\nঅনুমান করা হয়, কচুর উৎপত্তি ভারতীয় দ্বীপপুঞ্জসহ দক্ষিণ-পূর্ব এশিয়ায়। প্রায় দু'হাজার বছর আগেও কচুর চাষ হত বলে প্রমাণ পাওয়া গেছে। স্থলভূমি ও জলভুমি উভয় স্থানে কচু জন্মাতে পারে। তবে স্থলভাগে জন্মানো কচুর সংখ্যাই বেশি। কচুর বহু আয়ূর্বেদীয় গুনাগুন আছে বলে দাবি করা হয়। প্রজাতিভেদে কচুর মুল, শিকড় বা লতি, পাতা ও ডাটা সবই মানুষের খাদ্য। কচু শাকে প্রচুর পরিমাণে ভিটামিন এ থাকায় রাতকানা রোগ প্রতিরোধে এটি অত্যন্ত উপকারী।")
				.image(getImageAsByte("seed/tree-kochu.png"))
				.productivity("১ কেজি প্রতি মাস")
				.benefits("কচুর মূল উপাদান হলো আয়রন(Fe),যা রক্তে হিমোগ্লোবিনের মাত্রা ঠিক রেখে শরীরে অক্সিজেনের সরবরাহ ঠিক রাখে। প্রতি ১০০ গ্রাম কচুশাকে ৩৯ গ্রাম প্রোটিন, ৬.৮ গ্রাম শর্করা, ১৫ গ্রাম চর্বি, ২২৭ মিলি গ্রাম ক্যালশিয়াম, ১০ মিলি গ্রাম আয়রন, ৫৬ মিলিগ্রাম খাদ্যশক্তি থাকে। কচুতে অক্সালিক এসিডের ‍উপস্থিতি থাকায় খাবার পর মাঝে মাঝে গলা চুলকায়।")
				.icon(getImageAsByte("seed/icon-herbs.jpg"))
				.type(shak).build());
		Product puishak = productRepo.save(Product.builder().name("পুই শাক")
				.alternativeName("Hairy spinach").sequence(2)
				.description("পুঁই ( Basella alba) এক প্রকার লতা জাতীয় উদ্ভিদ। পুঁই গাছের পাতা ও ডাঁটি শাক হিসেবে খাওয়া হয় বলে সচরাচর একে পুঁই শাক হিসাবে উল্লেখ করা হয়। পুঁই Basellaceae গোত্রভুক্ত বহুবর্ষজীবী উষ্ণমণ্ডলীয় গাছ। বাংলাদেশে, পশ্চিমবঙ্গে, আসামে এবং ত্রিপুরায় সর্বত্র এর চাষ হয়ে থাকে। এর ভাজি এ সব এলাকার মানুষের প্রিয় সহযোগী খাদ্য।\n\nএটি নরম বহুশাখা যুক্ত উদ্ভিদ। এর মাংসল লতা দ্রুতবেগে দৈর্ঘে ১০ মিটার অবধি বাড়তে পারে। এর মোটা, অনেকটা রসালো, হরতন আকৃতির পাতাতে মৃদু সুগন্ধ আছে। পাতা মসৃণ, খানিকটা পিচ্ছিল ভাব আছে। পুঁই-এর একটি গোত্র লাল-পুঁইয়ের (Basella alba 'Rubra') এর পুর্ণবয়স্ক কাণ্ড লালচে বেগুনী রং এর যা লাল পুঁই ডাঁটা হিসাবে পরিচিত। পুঁইয়ের ফুল সাদা অথবা লাল। ফল মটর দানার মতো। পাকা ফল বেগুনি রঙের।\n\nচৈনিক ও বাঙালি রান্নায় এটি বহুল ব্যবহৃত। বাংলাদেশ, পশ্চিম বঙ্গ, আসাম ও ত্রিপুরায় পুঁই শাকের ভাজি দুপুরের প্রিয় খাদ্য। এচাড়ামাছ রাঁধতে পুঁই শাক ব্যবহার করা হয়। আফ্রিকায় অড়হড়ে পুঁই ডাঁটার রান্না প্রচলিত আছে। উত্তর ভিয়েতনামে কাঁকড়ার মাংস, ধুঁধুল (luffa) ও পাটশাকের (corchorus olitorius) সঙ্গে পুঁইয়ের সুপ খুব জনপ্রিয় খাবার। মালাবার উপকূলে সুপ ঘন করার জন্য ও রসুন ও লংকার সঙ্গে ভাজা হিসাবে এটি খাওয়ার প্রচলন আছে।")
				.image(getImageAsByte("seed/tree-pui.png"))
				.productivity("২ কেজি প্রতি মাস")
				.benefits("অন্যান্য অনেক শাকের মত এর মধ্যে অনেক ভিটামিন এ, ভিটামিন সি, লোহা, ও ক্যালসিয়াম আছে। এছাড়া ক্যালরির ঘনত্ব কম। তদুপরি ক্যালরি-প্রতি আমিষের পরিমাণও বেশী। এর মধ্যে ছিবড়ের পরিমাণ বেশী।\nপাতাসহ সমগ্র গাছ ভেষজ গুণাবলী সম্পন্ন। পাতা মূত্রকারক। গনোরিয়া রোগে এটি উপকারী। অর্শ রোগে অতিরিক্ত স্রাব, অতিসার প্রভৃতিতে অন্যান্য উপদানের সঙ্গে পুঁই শাকের ব্যবহার আছে। পুঁই শাকের পাতার রস ছোটদের সর্দি, কোষ্ঠবদ্ধতা প্রভৃতিতে উপকার দেয়। পুঁই শাকের রস মাখলে গোদে আরাম হয়।")
				.icon(getImageAsByte("seed/icon-herbs.jpg"))
				.type(shak).build());

		Product pepe = productRepo.save(Product.builder().name("পেঁপে")
				.alternativeName("Papaya").sequence(1)
				.description("এটি একটি ছোট আকৃতির অশাখ বৃক্ষবিশেষ। লম্বা বো‍টাঁযুক্ত ছত্রাকার পাতা বেশ বড় হয় এবং সর্পিল আকারে কান্ডের উপরি অংশে সজ্জিত থাকে। প্রায় সারা বছরেই ফুল ও ফল হয়। কাচাঁ ফল সবুজ, পাকা ফল হলুদ বা পীত বর্ণের। এটি পথ্য হিসেবে ও ব্যবহার হয়। কাচা পাকা দু ভাবেই খাওয়া যায়, তরে কাচা অবস্থায় সব্জি এবং পাকলে ফল। কাচা ফল বাইরের দিক গাঢ় কালচে সবুজ এবং পাকলে খোসা সহ কমলা রং ধারন করে।\n\nপেঁপে বাংলাদেশ, ভারত, আমেরিকা, ব্রাজিল ইত্যাদি দেশে হয়ে থাকে।\n\nবাংলাদেশের সর্বত্রই সবজি এবং ফলের জন্য চাষ করা হয়।")
				.image(getImageAsByte("seed/tree-pepe.png"))
				.productivity("২ কেজি প্রতি মাস")
				.benefits("রক্ত কাসে, রক্তার্শে, মূত্রনালীর ক্ষতে, দাদ ও সোরিয়াসিস-এ, কোষ্ঠকাঠিণ্যে এবং কৃমিতে হিতকর। পাকা পেঁপে অর্শরোগ ও কোষ্ঠকাঠিন্য রোগে হিতকর।")
				.icon(getImageAsByte("seed/icon-herbs.jpg"))
				.type(fruit).build());
		Product kola = productRepo.save(Product.builder().name("কলা")
				.alternativeName("Banana").sequence(2)
				.description("কলা এক প্রকারের বিশ্বব্যাপী জনপ্রিয় ফল। সাধারণত উষ্ণ জলবায়ু সম্পন্ন দেশসমূহে কলা ভাল জন্মায়। তবে দক্ষিণ-পূর্ব এশিয়াই কলার উৎপত্তিস্থল হিসাবে পরিগণিত। বাংলাদেশ সহ পৃথিবীর বহু দেশে কলা অন্যতম প্রধান ফল। বাংলাদেশের নরসিংদী, মুন্সীগঞ্জ, যশোর, বরিশাল, বগুড়া, রংপুর, জয়পুরহাট, কুষ্টিয়া, ঝিনাইদহ, মেহেরপুর, প্রভৃতি এলাকায় শত শত বৎসর যাবৎ ব্যাপকভাবে কলার চাষ হয়ে আসছে। বাংলাদেশে কলা চাষের সবচেয়ে বড় সুবিধা হল সারা বছর এ দেশের প্রায় সব অঞ্চলের উঁচু জমিতেই এর চাষ করা যায়। পার্বত্য এলাকায় বনকলা, বাংলাকলা, মামা কলাসহ বিভিন্ন ধরনের বুনোজাতের কলা চাষ হয়। কলম্বিয়া ইত্যাদি ল্যাটিন আমেরিকান দেশে কলা প্রধান অর্থকরী ফসল। প্রাগাধুনিক ভারতীয় অর্থনীতিতেও একটি প্রধান অর্থকরী ফসল হিসাবে কলার চাষাবাদ হতো। খনার বচনে আছে, \"কলা রুয়ে না কেটো পাত, তাতেই কাপড়, তাতেই ভাত\"।\n\nকাঁচা কলা সবুজ, পেকে গেলে তা হলুদ হয়ে যায়। কলাপাতা সরল, পত্রভিত পুরু ও পত্রফলক প্রশস্ত।। পত্রফলকে দৃঢ়, মোটা ও সুস্পষ্ট ও মধ্যশিরা বিদ্যমান। মধ্য শিরার দুই পাশে সমান্তরাল শিরাগুলো বিন্যাসিত । একান্তরক্রমে পাতাগুলোর উৎপত্তি ঘটে । পুষ্পমঞ্জুরী স্পেডিক্স ধরনের এবং নৌকার মত স্পেদ দ্বারা আবৃত থাকে। পুষ্পমঞ্জুরি গোড়ার দিকে ও আগার দিকে পুরুষ এবং নিরপেক্ষ ফুল থাকে। ফুল সাধারণত একপ্রতিসম উভলিঙ্গ। তবে কখনো কখনো একলিঙ্গ পুষ্পও দেখা যায় । ফুলের ব্রাক্টের রঙ অ্যান্থসায়ানিনের জন্য লালচে, গোলাপী বা বেগুনী হয়ে থাকে । ফুলে ছয়টি পাঁপড়ি পরস্পর ৩টি করে ২টি আবর্তে সজ্জিত থাকে। এগুলো যুক্ত বা পৃথক উভয়ভাবেই বিন্যস্ত থাকতে পারে। ফুলে পুংকেশর ৫টি, সবগুলোই উর্বর। যখন ৫টি দেখা যায় তখন অন্যটি অনুন্মোচিত বা অনুপস্থিত থাকে। স্ত্রী স্তবকের ৩টি গর্ভপত্র সংযুক্ত অবস্থায় দেখা যায় । ডিম্বাশয় অধোগর্ভ এবং তিনটি প্রকোষ্ঠ বিশিষ্ট। এর অমরাবিন্যাস অক্ষীয় ধরনের এবং ফল একক, সরস, ও বেরি(Berry) প্রকৃতির ।")
				.image(getImageAsByte("seed/tree-kola.png"))
				.productivity("১ কেজি প্রতি মাস")
				.benefits("কলা বিভিন্ন গুণাগুণে সমৃদ্ধ একটি ফল। এর পুষ্টিগুণ অধিক। এতে রয়েছে দৃঢ় টিস্যু গঠনকারী উপদান যথা আমিষ, ভিটামিন এবং খনিজ। কলা ক্যালরির একটি ভাল উৎস। এতে কঠিন খাদ্য উপাদান এবং সেই সাথে পানি জাতীয় উপাদান সমন্বয় যে কোন তাজা ফলের তুলনায় বেশি। একটি বড় মাপের কলা খেলে ১০০ ক্যালরির বেশি শক্তি পাওয়া যায়। কলাতে রয়েছে সহজে হজমযোগ্য শর্করা। এই শর্করা পরিপাকতন্ত্রকে সহজে হজম করতে সাহায্য করে। কলার মধ্যে থাকা আয়রন রক্তে হিমোগ্লোবিন উত্‍পাদনে সাহায্য করে। গবেষকরা জানান, রক্তচাপ নিয়ন্ত্রণ এবং স্বাভাবিক রক্তপ্রবাহ নিশ্চিত করতে দেহে পটাশিয়ামের উপস্থিতি অত্যন্ত জরুরি। এছাড়াও দেহে পটাসিয়ামের আদর্শ উপস্থিতি নিশ্চিত করা গেলে কমে যায় স্ট্রোকের ঝুঁকিও। আর এই উপকারী পটাশিয়াম কলায় আছে প্রচুর পরিমাণে। গবেষকরা দেখেছেন, একটি কলায় প্রায় ৫০০ মিলিগ্রাম পটাসিয়াম থাকে। আর মানবদেহে প্রতিদিন ১৬০০ মিলিগ্রাম পটাশিয়ামের যোগান দেয়া গেলেই স্ট্রোকের হাত থেকে বছরে বেঁচে যেতে পারে ১০ লক্ষ মানুষ।")
				.icon(getImageAsByte("seed/icon-fruit.jpg"))
				.type(fruit).build());

		NurseryType gov = nurseryTypeRepo.save(NurseryType.builder().name("সরকারী")
				.image(getImageAsByte("seed/nursery-logo-govt.jpg"))
				.sequence(1).build());
		NurseryType priv = nurseryTypeRepo.save(NurseryType.builder().name("বেসরকারী")
				.image(getImageAsByte("seed/nursery-logo-private.png"))
				.sequence(2).build());

		Nursery s1 = nurseryRepo.save(Nursery.builder().name("Uttara 11")
				.type(gov).area(uttara).address("Sector 11")
				.phone("+88 02 784354").sequence(1)
				.latitude(23.877316).longitude(90.39014).build());
		Nursery s2 = nurseryRepo.save(Nursery.builder().name("Uttara 3")
				.type(priv).area(uttara).address("Sector 3")
				.phone("+88 02 774354").sequence(2)
				.latitude(23.8654).longitude(90.397205).build());
		Nursery s3 = nurseryRepo.save(Nursery.builder().name("Dhanmondi 27")
				.type(gov).area(dhanmondi).address("Dhanmondi 27")
				.phone("+88 02 874354").sequence(1)
				.latitude(23.756242).longitude(90.375461).build());
		Nursery s4 = nurseryRepo.save(Nursery.builder().name("Dhanmondi Road 2")
				.type(priv).area(dhanmondi).address("Dhanmondi 2 no road")
				.phone("+88 02 884354").sequence(2)
				.latitude(23.739449).longitude(90.383057).build());
		Nursery s5 = nurseryRepo.save(Nursery.builder().name("মোল্লাপারা সরকারী নার্সারি")
				.type(gov).area(mirpur).address("১০ কাজিপারা এভিনিউ, মিরপুর ১০২০")
				.phone("+৮৮ ০১৭২৩ ৫৭২৮৯৬").sequence(1)
				.latitude(23.7971623).longitude(90.3705939).build());
		Nursery s6 = nurseryRepo.save(Nursery.builder().name("কিবরিয়া গাছ ভাণ্ডার")
				.type(priv).area(mirpur).address("২৩ শেওরাপারা, মিরপুর ১০২০")
				.phone("+৮৮ ০১৮৪৯ ৫৪৭৭৮৯").sequence(2)
				.latitude(23.7904029).longitude(90.3733791).build());

		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s1).price(new BigDecimal("80")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s1).price(new BigDecimal("200")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s1).price(new BigDecimal("50")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s2).price(new BigDecimal("70")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s2).price(new BigDecimal("180")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s2).price(new BigDecimal("40")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s3).price(new BigDecimal("65")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s3).price(new BigDecimal("190")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s3).price(new BigDecimal("55")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s4).price(new BigDecimal("75")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s4).price(new BigDecimal("210")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s4).price(new BigDecimal("45")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s5).price(new BigDecimal("65")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s5).price(new BigDecimal("200")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s5).price(new BigDecimal("40")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(tob).nursery(s6).price(new BigDecimal("75")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(drum).nursery(s6).price(new BigDecimal("210")).build());
		materialPriceRepo.save(MaterialPrice.builder().material(mati).nursery(s6).price(new BigDecimal("45")).build());

		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s1).price(new BigDecimal("20")).build());
		productPriceRepo.save(ProductPrice.builder().product(puishak).nursery(s1).price(new BigDecimal("20")).build());
		productPriceRepo.save(ProductPrice.builder().product(kochushak).nursery(s1).price(new BigDecimal("12")).build());
		productPriceRepo.save(ProductPrice.builder().product(pepe).nursery(s1).price(new BigDecimal("30")).build());
		productPriceRepo.save(ProductPrice.builder().product(kola).nursery(s1).price(new BigDecimal("50")).build());

		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s2).price(new BigDecimal("25")).build());
		productPriceRepo.save(ProductPrice.builder().product(puishak).nursery(s2).price(new BigDecimal("25")).build());
		productPriceRepo.save(ProductPrice.builder().product(kochushak).nursery(s2).price(new BigDecimal("15")).build());
		productPriceRepo.save(ProductPrice.builder().product(pepe).nursery(s2).price(new BigDecimal("25")).build());
		productPriceRepo.save(ProductPrice.builder().product(kola).nursery(s2).price(new BigDecimal("40")).build());

		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s3).price(new BigDecimal("30")).build());
		productPriceRepo.save(ProductPrice.builder().product(puishak).nursery(s3).price(new BigDecimal("30")).build());
		productPriceRepo.save(ProductPrice.builder().product(kochushak).nursery(s3).price(new BigDecimal("10")).build());
		productPriceRepo.save(ProductPrice.builder().product(pepe).nursery(s3).price(new BigDecimal("20")).build());
		productPriceRepo.save(ProductPrice.builder().product(kola).nursery(s3).price(new BigDecimal("45")).build());

		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s4).price(new BigDecimal("22")).build());
		productPriceRepo.save(ProductPrice.builder().product(puishak).nursery(s4).price(new BigDecimal("27")).build());
		productPriceRepo.save(ProductPrice.builder().product(kochushak).nursery(s4).price(new BigDecimal("14")).build());
		productPriceRepo.save(ProductPrice.builder().product(pepe).nursery(s4).price(new BigDecimal("23")).build());
		productPriceRepo.save(ProductPrice.builder().product(kola).nursery(s4).price(new BigDecimal("55")).build());

		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s5).price(new BigDecimal("20")).build());
		productPriceRepo.save(ProductPrice.builder().product(puishak).nursery(s5).price(new BigDecimal("20")).build());
		productPriceRepo.save(ProductPrice.builder().product(kochushak).nursery(s5).price(new BigDecimal("12")).build());
		productPriceRepo.save(ProductPrice.builder().product(pepe).nursery(s5).price(new BigDecimal("30")).build());
		productPriceRepo.save(ProductPrice.builder().product(kola).nursery(s5).price(new BigDecimal("50")).build());

		productPriceRepo.save(ProductPrice.builder().product(lalshak).nursery(s6).price(new BigDecimal("25")).build());
		productPriceRepo.save(ProductPrice.builder().product(puishak).nursery(s6).price(new BigDecimal("25")).build());
		productPriceRepo.save(ProductPrice.builder().product(kochushak).nursery(s6).price(new BigDecimal("15")).build());
		productPriceRepo.save(ProductPrice.builder().product(pepe).nursery(s6).price(new BigDecimal("25")).build());
		productPriceRepo.save(ProductPrice.builder().product(kola).nursery(s6).price(new BigDecimal("40")).build());
	}

	private byte[] getImageAsByte(String image) throws IOException {
		ClassPathResource backImgFile = new ClassPathResource(image);
		byte[] arrayPic = new byte[(int) backImgFile.contentLength()];
		backImgFile.getInputStream().read(arrayPic);
		return arrayPic;
	}

}
