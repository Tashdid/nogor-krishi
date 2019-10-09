package com.niil.nogor.krishi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niil.nogor.krishi.entity.Cart;
import com.niil.nogor.krishi.entity.CartDetails;
import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.CartDetailsRepo;
import com.niil.nogor.krishi.repo.CartRepo;
import com.niil.nogor.krishi.repo.OrderDetailsRepo;
import com.niil.nogor.krishi.repo.OrdersRepo;
import com.niil.nogor.krishi.repo.UserRepo;

@RestController
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(OrdersController.URL)
public class OrdersController extends AbstractController{
	static final String URL = "/test";
	
	@Autowired OrdersRepo ordersRepo;
	@Autowired OrderDetailsRepo orderDetailsRepo;
	@Autowired UserRepo userRepo;
	@Autowired CartDetailsRepo cartDetailsRepo;
	@Autowired CartRepo cartRepo;
	
	@RequestMapping(value="/orders/{userId}",method=RequestMethod.GET)
	public List<Orders> getOrders(@PathVariable Long userId) {
		
		 User user = userRepo.findOne(userId);
		 List<Orders> userOrders =  ordersRepo.findAllByUser(user);
		 if(!userOrders.isEmpty()){
			 return userOrders;
		 }
		 return null;
	}
	
	@PostMapping("/confirm-order/{userId}/cart/{cartId}")
	public ResponseEntity confirmOrder(@RequestBody Orders orders,Long userId,Long cartId) {
		
		Cart cart = cartRepo.findOne(cartId);
		List<CartDetails> cartDetails = cartDetailsRepo.findAllByCart(cart);
		
		/*
		 * place all cart items to order-details item and save 
		 */
		ordersRepo.save(orders);
		if(ConvertAndSaveCartToOrder(cartDetails)){
			return ResponseEntity.ok(HttpStatus.SC_OK);
		}
		return ResponseEntity.ok(HttpStatus.SC_BAD_REQUEST);
	}
	
	Boolean ConvertAndSaveCartToOrder(List<CartDetails> cartDetailList){
		List<OrderDetail> orderDetailsList = new ArrayList<OrderDetail>();
		
		for(int i=0;i<cartDetailList.size();i++){
			CartDetails cartDetail = cartDetailList.get(i);
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setNursery(cartDetail.getNursery());
			orderDetail.setProduct(cartDetail.getProduct());
			orderDetail.setQuantity(cartDetail.getQuantity());
			orderDetail.setUnit_price(cartDetail.getUnit_price());
			orderDetail.setSaleType(cartDetail.getSaleType());
			
			orderDetailsList.add(orderDetail);
		}
		if(orderDetailsRepo.save(orderDetailsList) != null){
			return true;
		}
		return false;
	}
}
