package com.niil.nogor.krishi.view;

import java.util.List;

import com.niil.nogor.krishi.entity.GardenBlock;
import com.niil.nogor.krishi.entity.GardenLayout;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jun 2, 2018
 *
 */
@Getter
@Setter
public class LayoutRQ extends GardenLayout {
	private String encodedImage;
	private List<GardenBlock> blocks;
}
