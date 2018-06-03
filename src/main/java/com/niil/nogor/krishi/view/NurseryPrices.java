package com.niil.nogor.krishi.view;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.niil.nogor.krishi.entity.*;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jun 3, 2018
 *
 */
public class NurseryPrices {
	ProductPrice productPrice;
	List<MaterialPrice> materialPrices;
	List<Long> productMaterialIds;
	private Comparator<MaterialPrice> byMaterialType = Comparator.comparing(
			parent -> parent.getMaterial().getType().getSequence()
	);

	private void setProductOnlyMaterialPrices() {
		if (productPrice == null || materialPrices == null) return;
		List<Long> pmids = productPrice.getProduct().getMaterials().stream().map(m -> m.getId()).collect(Collectors.toList());
		materialPrices = materialPrices.stream().filter(mp -> pmids.contains(mp.getMaterial().getId()))
				.sorted(byMaterialType).collect(Collectors.toList());
	}

	public ProductPrice getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
		setProductOnlyMaterialPrices();
	}

	public List<MaterialPrice> getMaterialPrices() {
		return materialPrices;
	}

	public void setMaterialPrices(List<MaterialPrice> materialPrices) {
		this.materialPrices = materialPrices;
		setProductOnlyMaterialPrices();
	}

	public BigDecimal getTotal() {
		return productPrice.getPrice().add(materialPrices.stream().map(mp -> mp.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
	}
}
