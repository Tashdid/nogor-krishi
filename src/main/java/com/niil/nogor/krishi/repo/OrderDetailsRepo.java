package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;

@Repository
public interface OrderDetailsRepo extends JpaRepository<OrderDetail, Long>{
	
	List<OrderDetail> findAllByOrdersAndNursery(Orders order,Nursery nursery);

}
