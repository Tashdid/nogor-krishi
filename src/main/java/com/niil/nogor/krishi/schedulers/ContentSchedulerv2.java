package com.niil.nogor.krishi.schedulers;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.ProductType;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.ProductTypeRepo;


import krishi.gov.api.KrishiAPIv2;
import krishi.gov.api.models.APIResponse;
import krishi.gov.api.models.APIResponsev2;
import krishi.gov.api.models.Categories;
import krishi.gov.api.models.Crops;
import krishi.gov.api.models.CropVarieties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ContentSchedulerv2 {

	@Autowired private KrishiAPIv2 krishiAPIv2;
	@Autowired ProductTypeRepo productTypeRepo;
	@Autowired ProductRepo productRepo;

	@Scheduled(cron="0 0 1 * * *")  //Every day at 1:0:0 am
	public void loadCategoriesAndCrops() {
		log.debug("Loading contents scheduler starts at {}", LocalDateTime.now());
		try {

			//categories to productType
			APIResponse<Categories> resp = krishiAPIv2.getCategories();
			log.debug("Category count {} at {}", resp.getData().size(), LocalDateTime.now());
			
			resp.getData().forEach(cat->{
				ProductType bean;
				
				bean = productTypeRepo.findTopByNameOrderBySequenceDesc(cat.getCategoryName());
				
				if (null == bean) {
					ProductType lt = productTypeRepo.findTopByOrderBySequenceDesc();
					int seq = (lt == null ? 0 : lt.getSequence()) + 1;
					bean = ProductType.builder().sequence(seq).build();
					
					bean.setName(cat.getCategoryName());
				}
				bean.setCategoryTitle(cat.getCategoryTitle());
				
				productTypeRepo.save(bean);
				
			});
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error at {} - {}", LocalDateTime.now(), ex.getMessage());
		}
		
	}
	
	@Scheduled(cron="0 20 1 * * *")   //Every day at 1:20:0 am
	public void loadCropsParent() {
		log.debug("Loading contents scheduler starts at {}", LocalDateTime.now());
		try {
		//crops to product 
		APIResponse<Crops> respCrops = krishiAPIv2.getCrops();
		log.debug("Parent product count {} at {}", respCrops.getData().size(), LocalDateTime.now());
		
		respCrops.getData().forEach(crs ->{

			System.out.println(crs.getName()+"***"+crs.getApiId());
			Product bean;
			bean = productRepo.findTopByApiIdOrderBySequenceAsc(crs.getApiId());
			ProductType pType=productTypeRepo.findTopByNameOrderBySequenceDesc(crs.getCategory());
			if (null == bean) {
				Product lt = productRepo.findTopByTypeOrderBySequenceDesc(pType);
				int seq = (lt == null ? 0 : lt.getSequence()) + 1;
				bean = Product.builder().sequence(seq).build();
			}
			bean.setApiId(crs.getApiId());
			bean.setName(crs.getName());
			bean.setScientificName(crs.getScientificName());
			bean.setCategory(crs.getCategory());
			bean.setThumbsnail(crs.getThumbsnail());
			bean.setType(pType);

			productRepo.save(bean);
			
		});
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error at {} - {}", LocalDateTime.now(), ex.getMessage());
		}
	}
	
	@Scheduled(cron="0 50 1 * * *")  //Every day at 1:50:0 am
	public void loadCropChild() {
		
		List<Product> parentList=productRepo.findAllByApiIdIsNotNullAndParentIsNull();
		System.out.println("Size:"+parentList.size());
		
		int count=0;
		
		for(Product crs:parentList){
			count++;
			if(count%30==0) {
				try {
					 
					TimeUnit.MINUTES.sleep(new Long(1));
					
				}catch(Exception ex) {ex.printStackTrace();}
			}
			
			//crops to child product 
			APIResponsev2<CropVarieties> respChild = krishiAPIv2.getCropByCropId(crs.getApiId().intValue());
			
			if(null==respChild.getData()|| respChild.getData().getVarieties()==null|| respChild.getData().getVarieties().isEmpty()) {
				log.debug("Child product: Data not found for {} at {}",crs.getApiId(), LocalDateTime.now());
				
			}else {

			//System.out.println(respChild.getData().getVarieties().size());
			log.debug("Child product count {} for {} at {}", respChild.getData().getVarieties().size(),crs.getApiId(), LocalDateTime.now());
			
			respChild.getData().getVarieties().forEach(child->{

				//System.out.println(child.getApiId()+"********"+child.getName());
				Product beanChild;
				beanChild = productRepo.findTopByCApiIdOrderBySequenceAsc(child.getApiId());
				
				if (null == beanChild) {
					Product lt = productRepo.findTopByTypeOrderBySequenceDesc(crs.getType());
					int seq = (lt == null ? 0 : lt.getSequence()) + 1;
					beanChild = Product.builder().sequence(seq).build();
				}
				beanChild.setCApiId(child.getApiId());
				beanChild.setName(child.getName());
				beanChild.setType(crs.getType());
				beanChild.setParent(crs);
				beanChild.setThumbsnail(child.getThumbsnail());

				beanChild.setReleasedBy(child.getReleasedBy());
				beanChild.setSeriesNo(child.getSeriesNo());
				beanChild.setLocalName(child.getLocalName());
				beanChild.setProductionWithIrrigation(child.getProductionWithIrrigation());
				beanChild.setProductionWithoutIrrigation(child.getProductionWithoutIrrigation());
				beanChild.setLifeTime(child.getLifeTime());

				productRepo.save(beanChild);
			});
			}
			
		}
		log.debug("Child product: Data migration completed for at {}",LocalDateTime.now());
		
		
	}

}
