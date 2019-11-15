package com.niil.nogor.krishi.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.niil.nogor.krishi.dto.OrderForm;
import com.niil.nogor.krishi.entity.CartDetails;
import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.repo.CartDetailsRepo;
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
	
	public String currentUserName(Principal principal) {
	     return principal.getName();
	  }
	
	@RequestMapping(value="/orders/{userId}",method=RequestMethod.GET)
	public List<Orders> getOrders(@PathVariable Long userId) {
		
		 User user = userRepo.findOne(userId);
		 List<Orders> userOrders =  ordersRepo.findAllByUser(user);
		 if(!userOrders.isEmpty()){
			 return userOrders;
		 }
		 return null;
	}
	
	@RequestMapping(value="/confirm-order/",method=RequestMethod.POST)
	public Orders confirmOrder(@RequestBody OrderForm orderForm, HttpSession httpSession) { // @todo transection missing
		
		if(httpSession.getAttribute("cartId") == null){
			return null;
		 }
		
		List<CartDetails> cartDetailsList = cartDetailsRepo.findAllBysessionId(
				(String)httpSession.getAttribute("cartId"));
		
		return ConvertAndSaveCartToOrder(cartDetailsList,orderForm);
	}
	
	Orders ConvertAndSaveCartToOrder(List<CartDetails> cartDetailList,OrderForm  orderForm){
		
		Orders newOrder = new Orders();
		newOrder.setAddress(orderForm.getAddress());
		newOrder.setPhone_no(orderForm.getPhoneNo());
		newOrder.setOrder_time(LocalDateTime.now());
		newOrder.setStatus("new");
		newOrder.setPayable_amount(orderForm.getTotal_price());
		newOrder.setOrders_id(new Long(12110));
		newOrder.setUser(userRepo.findByMobile(orderForm.getPhoneNo()));///// @todo use mobile of loging user
		newOrder.setComment("New order test comment");
		newOrder.setRating(3);
		
		ordersRepo.save(newOrder);
		List<OrderDetail> orderDetailsList = new ArrayList<OrderDetail>();
		
		for(int i=0;i<cartDetailList.size();i++){
			CartDetails cartDetail = cartDetailList.get(i);
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setNursery(cartDetail.getNursery());
			orderDetail.setProduct(cartDetail.getProduct());
			orderDetail.setQuantity(cartDetail.getQuantity());
			orderDetail.setUnit_price(cartDetail.getUnit_price());
			orderDetail.setSaleType(cartDetail.getSaleType());
			orderDetail.setOrders(newOrder);
			orderDetailsList.add(orderDetail);
		}
		if(orderDetailsRepo.save(orderDetailsList) != null){
			return newOrder;
		}
		return null;
	}
}
