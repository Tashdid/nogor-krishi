package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.User;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long>{
	
	List<Orders> findAllByUser(User user);

	@Query("SELECT distinct o.orders FROM OrderDetail o where o.nursery.id=:nurseryId group by o.orders.id having count(o.orders.id)>=1")
	List<Orders> findDistinctOrderByNursery(@Param("nurseryId") Long nurseryId);
}
