package com.niil.nogor.krishi.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.entity.MaterialPrice;
import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.repo.DeliveryPersonRepo;
import com.niil.nogor.krishi.repo.MaterialPriceRepo;
import com.niil.nogor.krishi.repo.NurseryRepo;
import com.niil.nogor.krishi.repo.OrderDetailsRepo;
import com.niil.nogor.krishi.repo.OrdersRepo;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.service.SecurityService;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Sep 22, 2018
 *
 */
@Controller
@RequestMapping(VendorPriceController.URL)
public class VendorPriceController extends AbstractController {
	
	
	
	static final String URL = "/vendorprice";

	@Autowired NurseryRepo nurseryRepo;
	@Autowired OrdersRepo orderRepo;
	@Autowired OrderDetailsRepo orderDetailsRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired SecurityService securityService;
	@Autowired NurseryController nurserController;
	@Autowired UserRepo userRepo;
	@Autowired DeliveryPersonRepo deliveryPersonRepo;
	
	

	@GetMapping
	public String vendorPricePage(final ModelMap model) {
		model.addAttribute("basePath", URL + "/");
		Nursery bean = securityService.findLoggedInUser().getNursery();
		model.addAttribute("bean", bean);
		nurserController.loadNurseryPrices(bean, model);
		return URL.substring(1);
	}
	
	@GetMapping("/vendor-order-list")
	public String vendorOrderPage(final ModelMap model) {
		Set<Orders> ordersList = new TreeSet<Orders>();
		List<OrderDetail> orderDetailsList = orderDetailsRepo.findAllByNursery(userRepo.
				findByMobile(SecurityContextHolder.getContext().getAuthentication().getName()).getNursery());
		for (OrderDetail o:orderDetailsList){
			ordersList.add(o.getOrders());
		}
		model.addAttribute("orders", ordersList);
		return "nursery/order_list";
	}
	
	@GetMapping("/vendor-order-detail/{order_id}")
	public String vendorOrderDetailsPage(@PathVariable Long order_id,final ModelMap model) {
		Orders order = orderRepo.findOne(order_id);
		List<OrderDetail> orderDetailsList = orderDetailsRepo.findAllByOrdersAndNursery(order,userRepo.
				findByMobile(SecurityContextHolder.getContext().getAuthentication().getName()).getNursery());
		model.addAttribute("orderDetailList", orderDetailsList);
		model.addAttribute("order",order);
		model.addAttribute("deliveryPersonList", deliveryPersonRepo.findAll());
		return "nursery/order_details";
	}

	@RequestMapping(value="/{code}/product", method=RequestMethod.POST)
	public String addProductPrice(@PathVariable Long code, ProductPrice mprice, RedirectAttributes redirectAttrs) {
		nurserController.addProductPrice(code, mprice, redirectAttrs);
		return "redirect:" + URL;
	}

	@RequestMapping(value="/{code}/product/{product}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteProductPrice(@PathVariable Long code, @PathVariable Long product) {
		return nurserController.deleteProductPrice(code, product);
	}

	@RequestMapping(value="/{code}/material", method=RequestMethod.POST)
	public String addMaterialPrice(@PathVariable Long code, MaterialPrice mprice, RedirectAttributes redirectAttrs) {
		nurserController.addMaterialPrice(code, mprice, redirectAttrs);
		return "redirect:" + URL;
	}

	@RequestMapping(value="/{code}/material/{material}", method=RequestMethod.POST)
	@ResponseBody
	public Boolean deleteMaterialPrice(@PathVariable Long code, @PathVariable Long material) {
		return nurserController.deleteMaterialPrice(code, material);
	}

	@RequestMapping(value="/{nursery}/product/price", method=RequestMethod.GET)
	public ResponseEntity<byte[]> exportProductPrice(@PathVariable Nursery nursery) {
		return nurserController.exportProductPrice(securityService.findLoggedInUser().getNursery());
	}

	@RequestMapping(value="/{nursery}/material/price", method=RequestMethod.GET)
	public ResponseEntity<byte[]> exportMaterialPrice(@PathVariable Nursery nursery) {
		return nurserController.exportMaterialPrice(securityService.findLoggedInUser().getNursery());
	}

	@RequestMapping(value="/{nursery}/product/price", method=RequestMethod.POST)
	public String importProductPrice(@PathVariable Nursery nursery, MultipartFile file, RedirectAttributes redirectAttrs) {
		nurserController.importProductPrice(securityService.findLoggedInUser().getNursery(), file, redirectAttrs);
		return "redirect:" + URL;
	}

	@RequestMapping(value="/{nursery}/material/price", method=RequestMethod.POST)
	public String importMaterialPrice(@PathVariable Nursery nursery, MultipartFile file, RedirectAttributes redirectAttrs) {
		nurserController.importMaterialPrice(securityService.findLoggedInUser().getNursery(), file, redirectAttrs);
		return "redirect:" + URL;
	}
	
	@RequestMapping(value = "/vendor-order-detail/update-order", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity updateOrder(@ModelAttribute  Orders type) {

		Orders bean;
		System.out.println(type.getId());
		bean = orderRepo.findOne(type.getId());
		if (type.getId() != null && (bean = orderRepo.findOne(type.getId())) != null) {

			if (type.getStatus() != null) {
				bean.setStatus(type.getStatus());
			}
			if (type.getDelivery_datest() != null) {
				String str = type.getDelivery_datest();
				SimpleDateFormat sFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				try {
					bean.setDelivery_date(sFormat.parse(str));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if (type.getDelivery_person() != null) {
				bean.setDelivery_person(type.getDelivery_person());
			}

			bean = orderRepo.save(bean);

			return ResponseEntity.ok(HttpStatus.SC_OK);
		}

		return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
}
