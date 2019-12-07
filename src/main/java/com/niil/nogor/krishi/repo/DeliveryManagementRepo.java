package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.DeliveryManagement;
import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;

@Repository
public interface DeliveryManagementRepo extends JpaRepository<DeliveryManagement, Long>{
	
	List<DeliveryManagement> findAllByOrdersAndNursery(Orders order,Nursery nursery);
	List<DeliveryManagement> findAllByOrders(Orders order);

}
