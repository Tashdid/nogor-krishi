package com.niil.nogor.krishi.view;

import java.util.List;

import com.niil.nogor.krishi.entity.Area;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 8, 2018
 *
 */
@Data
public class LArea {
	private Area area;
	private List<LNursery> nurseries;
}
