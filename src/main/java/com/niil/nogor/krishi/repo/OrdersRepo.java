package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.entity.User;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long>{
	
	List<Orders> findAllByUser(User user);
}
