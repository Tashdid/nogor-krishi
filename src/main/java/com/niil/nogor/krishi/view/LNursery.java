package com.niil.nogor.krishi.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.niil.nogor.krishi.entity.MaterialPrice;
import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.ProductPrice;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 8, 2018
 *
 */
@Data
public class LNursery {
	private Nursery nursery;
	private List<ProductPrice> productPrices;
	private Map<String, List<MaterialPrice>> materialPrices;

	public BigDecimal getTotal() {
		BigDecimal tot = BigDecimal.ZERO;
		if (productPrices != null && !productPrices.isEmpty()) {
			tot = tot.add(productPrices.get(0).getPrice());
		}
		if (materialPrices != null && !materialPrices.isEmpty()) {
			tot = tot.add(materialPrices.values().stream().filter(ls -> !ls.isEmpty()).map(ls -> ls.get(0).getPrice()).reduce(BigDecimal::add).get());
		}
		return tot;
	}
}
