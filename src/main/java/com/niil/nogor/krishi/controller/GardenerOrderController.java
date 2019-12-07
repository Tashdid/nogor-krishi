package com.niil.nogor.krishi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.repo.MaterialPriceRepo;
import com.niil.nogor.krishi.repo.NurseryRepo;
import com.niil.nogor.krishi.repo.OrderDetailsRepo;
import com.niil.nogor.krishi.repo.OrdersRepo;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.service.SecurityService;

@Controller
@RequestMapping(GardenerOrderController.URL)
public class GardenerOrderController extends AbstractController{
	
	static final String URL = "/gardener";

	@Autowired NurseryRepo nurseryRepo;
	@Autowired OrdersRepo orderRepo;
	@Autowired OrderDetailsRepo orderDetailsRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired SecurityService securityService;
	@Autowired NurseryController nurserController;
	@Autowired UserRepo userRepo;
	
	
	@GetMapping("/gardener-order-list")
	public String gardenerOrderPage(final ModelMap model) {
		List<Orders> ordersList= orderRepo.findAllByUser(userRepo.
				findByMobile(SecurityContextHolder.getContext().getAuthentication().getName()));
		model.addAttribute("orders", ordersList);
		return "site/gardener_order_list";
	}
	// @todo enable comment
	 @GetMapping("/gardener-order-detail/{order_id}")
	 public String vendorOrderDetailsPage(@PathVariable Long order_id,final ModelMap model) {
	 	Orders order = orderRepo.findOne(order_id);
	 	List<OrderDetail> orderDetailsList = orderDetailsRepo.findAllByOrders(order);
	 	model.addAttribute("orderDetailList", orderDetailsList);
	 	model.addAttribute("order", order);
	 	return "site/gardener_order_details";
	 }

}
