package com.niil.nogor.krishi.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.APIContent;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Apr 28, 2018
 *
 */
@Repository
public interface APIContentRepo extends JpaRepository<APIContent, Long> {

	List<APIContent> findAllByPublishedTrue();

	List<APIContent> findAllByPublishedTrueOrderBySequenceAsc();

	List<APIContent> findAllByPublishedFalse();

	List<APIContent> findAllByOrderBySequenceAsc();

	List<APIContent> findTop5ByPublishedTrueOrderBySequenceAsc();

	List<APIContent> findTop10ByPublishedTrueOrderBySequenceAsc();

	Optional<APIContent> findByApiId(int apiId);

	Optional<APIContent> findTopByOrderBySequenceDesc();
	
	List<APIContent> findByPublishedTrueAndTitleIgnoreCaseContaining(String title);
}
