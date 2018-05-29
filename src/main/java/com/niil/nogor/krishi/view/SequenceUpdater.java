package com.niil.nogor.krishi.view;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since May 2, 2018
 *
 */
@Data
public class SequenceUpdater {
	private Map<Long, Integer> data = new HashMap<>();
}
