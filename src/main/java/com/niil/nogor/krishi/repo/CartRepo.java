package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Cart;
import com.niil.nogor.krishi.entity.User;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long>{
	//List<Cart> findAllbyUser(User user);

}
