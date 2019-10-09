package com.niil.nogor.krishi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.repo.NurseryRepo;
import com.niil.nogor.krishi.repo.OrderDetailsRepo;
import com.niil.nogor.krishi.repo.OrdersRepo;

@RestController
@RequestMapping(OrdersController.URL)
public class OrderDetailController extends AbstractController{
static final String URL = "/test";
	
	@Autowired OrdersRepo ordersRepo;
	@Autowired NurseryRepo nurseryRepo;
	@Autowired OrderDetailsRepo orderDetailsRepo;
	
	@RequestMapping(value="/orderdetails/{orderId}/nursery/{nurseryId}",method=RequestMethod.GET)
	public List<OrderDetail> getOrders(@PathVariable Long orderId,@PathVariable Long nurseryId) {
		
		 Orders order = ordersRepo.findOne(orderId);
		 Nursery nursery = nurseryRepo.findOne(nurseryId);
		 List<OrderDetail> orderDetails = orderDetailsRepo.findAllByOrdersAndNursery(order,nursery);
		 if(!orderDetails.isEmpty()){
			 return orderDetails;
		 }
		 return null;
	}

}
