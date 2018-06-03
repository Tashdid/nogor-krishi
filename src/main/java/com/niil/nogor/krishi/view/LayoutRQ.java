package com.niil.nogor.krishi.view;

import java.util.List;

import com.niil.nogor.krishi.entity.GardenBlock;
import com.niil.nogor.krishi.entity.GardenLayout;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jun 2, 2018
 *
 */
public class LayoutRQ extends GardenLayout {
	private List<GardenBlock> blocks;

	public List<GardenBlock> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<GardenBlock> blocks) {
		this.blocks = blocks;
	}
}
