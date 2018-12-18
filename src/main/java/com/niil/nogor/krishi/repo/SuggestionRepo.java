package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Suggestion;
import com.niil.nogor.krishi.entity.User;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 16, 2018
 *
 */
@Repository
public interface SuggestionRepo extends JpaRepository<Suggestion, Long> {

	List<Suggestion> findAllByPublishedTrueOrderByViewCountDesc();

	List<Suggestion> findTop10ByPublishedTrueOrderByViewCountDesc();

	List<Suggestion> findAllByUserOrderByCreatedOnDesc(User user);

	List<Suggestion> findAllByPublishedTrueOrderByCreatedOnDesc();
}
