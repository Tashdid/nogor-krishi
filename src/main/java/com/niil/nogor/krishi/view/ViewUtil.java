package com.niil.nogor.krishi.view;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.niil.nogor.krishi.entity.Menu;
import com.niil.nogor.krishi.entity.Page;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 5, 2018
 *
 */
@Component("vutil")
public class ViewUtil {

	public String titleToPath(String title) {
		return title.replaceAll("\\s+", "_");
	}

	public List<Page> getPages(Map<Menu, List<Page>> pagesMap, Menu menu) {
		return pagesMap.get(menu);
	}

	public String genUrl(List<Page> pages) {
		return pages.isEmpty() ? "#" : ("/pages/" + pages.get(0).getId() + "/" + titleToPath(pages.get(0).getTitle()));
	}
}
