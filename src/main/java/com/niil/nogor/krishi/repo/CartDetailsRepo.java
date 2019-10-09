package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Cart;
import com.niil.nogor.krishi.entity.CartDetails;

@Repository
public interface CartDetailsRepo extends JpaRepository<CartDetails, Long>{
	List<CartDetails> findAllByCart(Cart cart);

}
