package com.niil.nogor.krishi.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.entity.ProductPriceOnPropertyValue;

@Repository
public class ProductPriceRepoImpl implements ProductPriceCustomRepo {

	

    @PersistenceContext
    EntityManager entityManager;
	
	@Override
	public List<ProductPrice> getProductPriceListByPropertyValue(Long productId,List<String> propertyValueIdList) {
		// TODO Auto-generated method stub
		String querySt="SELECT pp.* FROM product_price_on_property_value em inner join product_price pp on em.product_price_id=pp.id "
				+ " where pp.product_id=:product_id and (";
		
		for(String id:propertyValueIdList){
			if(propertyValueIdList.indexOf(id) != 0) {
				querySt=querySt.concat(" or ");
			}
			querySt=querySt.concat(" em.product_property_value_id="+id+"");
		};
		
		querySt=querySt.concat(") group by pp.id " + 
				" having count(pp.id)>="+propertyValueIdList.size());
		//select ppn_id  from price_product_nursery_variable 
		//where property_value_id = 2 or property_value_id = 5
				//group by ppn_id
				//having (count(ppn_id) = 2);
		Query query = entityManager.createNativeQuery(querySt,ProductPrice.class);
//                "WHERE em LIKE ?", ProductPrice.class);
        query.setParameter("product_id", productId);
        System.out.println("------------------------------------------------");
        System.out.println(querySt);
        return query.getResultList();
	}

}
