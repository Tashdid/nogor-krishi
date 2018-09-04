package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Menu;
import com.niil.nogor.krishi.entity.Page;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface PageRepo extends JpaRepository<Page, Long> {

	List<Page> findAllByPublishedTrueOrderBySequenceAsc();

	List<Page> findAllByMenuOrderBySequenceAsc(Menu menu);
	
	Page findTopByMenuOrderBySequenceDesc(Menu menu);

	List<Page> findTop10ByPublishedTrueOrderByViewCountDesc();
}
