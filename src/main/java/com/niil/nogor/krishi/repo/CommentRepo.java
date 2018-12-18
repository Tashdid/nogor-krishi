package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Comment;
import com.niil.nogor.krishi.entity.Suggestion;
import com.niil.nogor.krishi.entity.User;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Dec 16, 2018
 *
 */
@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

	List<Comment> findAllBySuggestionOrderByCreatedOn(Suggestion suggestion);

	List<Comment> findAllByUserOrderByCreatedOnDesc(User user);
}
