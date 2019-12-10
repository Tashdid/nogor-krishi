package com.niil.nogor.krishi.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niil.nogor.krishi.entity.Comment;
import com.niil.nogor.krishi.entity.DeliveryManagement;
import com.niil.nogor.krishi.entity.MaterialPrice;
import com.niil.nogor.krishi.entity.NotesOnOrder;
import com.niil.nogor.krishi.entity.Nursery;
import com.niil.nogor.krishi.entity.OrderDetail;
import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.entity.ProductPrice;
import com.niil.nogor.krishi.entity.ProductPriceOnPropertyValue;
import com.niil.nogor.krishi.entity.ProductProperty;
import com.niil.nogor.krishi.entity.ProductPropertyValue;
import com.niil.nogor.krishi.entity.Settings;
import com.niil.nogor.krishi.repo.DeliveryManagementRepo;
import com.niil.nogor.krishi.repo.DeliveryPersonRepo;
import com.niil.nogor.krishi.repo.MaterialPriceRepo;
import com.niil.nogor.krishi.repo.NotesOnOrderRepo;
import com.niil.nogor.krishi.repo.NurseryRepo;
import com.niil.nogor.krishi.repo.OrderDetailsRepo;
import com.niil.nogor.krishi.repo.OrdersRepo;
import com.niil.nogor.krishi.repo.ProductPriceRepo;
import com.niil.nogor.krishi.repo.UserRepo;
import com.niil.nogor.krishi.service.SecurityService;


@Controller
@RequestMapping(AllOrderController.URL)
public class AllOrderController extends AbstractController {
	
	
	
	static final String URL = "/manage/orders";

	@Autowired NurseryRepo nurseryRepo;
	@Autowired OrdersRepo orderRepo;
	@Autowired OrderDetailsRepo orderDetailsRepo;
	@Autowired MaterialPriceRepo materialPriceRepo;
	@Autowired ProductPriceRepo productPriceRepo;
	@Autowired SecurityService securityService;
	@Autowired NurseryController nurserController;
	@Autowired UserRepo userRepo;
	@Autowired DeliveryPersonRepo deliveryPersonRepo;
	@Autowired NotesOnOrderRepo notesOnOrderRepo;
	@Autowired DeliveryManagementRepo deliveryManagementRepo;
	
	

	@GetMapping
	public String vendorPricePage(final ModelMap model) {
		model.addAttribute("basePath", URL + "/");
		Nursery bean = securityService.findLoggedInUser().getNursery();
		model.addAttribute("bean", bean);
		nurserController.loadNurseryPrices(bean, model);
		return URL.substring(1);
	}
	
	@GetMapping("/all-order-list")
	public String vendorOrderPage(final ModelMap model) {
		List<Orders> ordersList = new ArrayList<Orders>();
		ordersList=orderRepo.findAll();
		model.addAttribute("orders", ordersList);
		return "manage/order_list";
	}
	
	@GetMapping("/all-order-detail/{order_id}")
	public String vendorOrderDetailsPage(@PathVariable Long order_id,final ModelMap model) {
		Orders order = orderRepo.findOne(order_id);
		List<OrderDetail> orderDetailsList = orderDetailsRepo.findAllByOrders(order);
		List<Nursery> nurseryList=new ArrayList<>();
		orderDetailsList.forEach(detailOrder->{
			if(!nurseryList.contains(detailOrder.getNursery()) && detailOrder.getNursery()!=null && detailOrder.getNursery().getId()!=null) {
				nurseryList.add(detailOrder.getNursery());
			}
		});
		
		List<DeliveryManagement> deliveryManagements=deliveryManagementRepo.findAllByOrders(order);
		Set<Nursery> allNurseriesFromDelivery = deliveryManagements.stream()
				.map(DeliveryManagement::getNursery).collect(Collectors.toSet());
		
		nurseryList.forEach(nursery->{
			if(!allNurseriesFromDelivery.contains(nursery)) {
				DeliveryManagement deliveryManagement=new DeliveryManagement();
				deliveryManagement.setNursery(nursery);
				deliveryManagements.add(deliveryManagement);
			}
		});
		int nurseryCount=allNurseriesFromDelivery.size();
		
		BigDecimal totalPrice = new BigDecimal(0);
		for(OrderDetail orderDetail:orderDetailsList){
			totalPrice = totalPrice.add(orderDetail.getUnit_price().multiply(new BigDecimal(orderDetail.getQuantity())));
		}
		Settings settings = settingsRepo.findAll().stream().findFirst().orElse(new Settings());
		
		order.setTotal_amount(totalPrice);
		order.setDelivery_charge(settings.getDeliveryCharge()*nurseryCount);
		BigDecimal totalPayable = new BigDecimal(0);
		totalPayable=totalPayable.add(totalPrice);
		totalPayable=totalPayable.add(new BigDecimal(order.getDelivery_charge()));
		order.setPayable_amount(totalPayable);
		
		deliveryManagements.forEach(dm->{
			if(dm.getDeliveryDate()!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
				String dateSt=sdf.format(dm.getDeliveryDate());
				dm.setDeliveryDatest(dateSt);
			}
		});
		
		order.getDeliveryManagements().clear();
		order.getDeliveryManagements().addAll(deliveryManagements);
		model.addAttribute("orderDetailList", orderDetailsList);
		model.addAttribute("order",order);
		model.addAttribute("nurseryList",nurseryList);
		model.addAttribute("deliveryPersonList", deliveryPersonRepo.findAll());
		return "manage/order_details";
	}
	
	@GetMapping("/print-all-order/{order_id}")
	public String vendorOrderDetailsPrint(@PathVariable Long order_id,final ModelMap model) {
		System.out.println("print***");
		Orders order = orderRepo.findOne(order_id);
		List<OrderDetail> orderDetailsList = orderDetailsRepo.findAllByOrders(order);
		
		List<DeliveryManagement> deliveryManagements=deliveryManagementRepo.findAllByOrders(order);
		
		Set<Nursery> allNurseriesFromDelivery = deliveryManagements.stream()
				.map(DeliveryManagement::getNursery).collect(Collectors.toSet());

		int nurseryCount=allNurseriesFromDelivery.size();
		
		BigDecimal totalPrice = new BigDecimal(0);
		for(OrderDetail orderDetail:orderDetailsList){
			totalPrice = totalPrice.add(orderDetail.getUnit_price().multiply(new BigDecimal(orderDetail.getQuantity())));
		}
		Settings settings = settingsRepo.findAll().stream().findFirst().orElse(new Settings());
		
		order.setTotal_amount(totalPrice);
		order.setDelivery_charge(settings.getDeliveryCharge()*nurseryCount);
		BigDecimal totalPayable = new BigDecimal(0);
		totalPayable=totalPayable.add(totalPrice);
		totalPayable=totalPayable.add(new BigDecimal(order.getDelivery_charge()));
		order.setPayable_amount(totalPayable);
		
		deliveryManagements.forEach(dm->{
			if(dm.getDeliveryDate()!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
				String dateSt=sdf.format(dm.getDeliveryDate());
				dm.setDeliveryDatest(dateSt);
			}
		});
		
		model.addAttribute("orderDetailList", orderDetailsList);
		model.addAttribute("order",order);
		model.addAttribute("deliveryManagements",deliveryManagements==null||deliveryManagements.isEmpty()?new DeliveryManagement():deliveryManagements);
		return "manage/order_print";
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
	
	@RequestMapping(value = "/all-order-detail/update-order", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity updateOrder(@ModelAttribute Orders type) {

		Orders bean;
		System.out.println(type.getId());
		bean = orderRepo.findOne(type.getId());
		if (type.getId() != null && (bean = orderRepo.findOne(type.getId())) != null) {

			if (type.getStatus() != null) {
				bean.setStatus(type.getStatus());
			}
			
			if (type.getFeedbackSt() != null) {
				bean.setFeedbackSt(type.getFeedbackSt());
				bean.setFeedbackDate(LocalDateTime.now());
			}

			bean = orderRepo.save(bean);

			return ResponseEntity.ok(HttpStatus.SC_OK);
		}

		return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "all-order-detail/{order_id}/add-notes", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity updateOrder(@PathVariable Long order_id, @ModelAttribute NotesOnOrder type) {
		try {
			System.out.println(order_id);
			Orders bean = orderRepo.findOne(order_id);
			if (bean != null) {
				NotesOnOrder notesOnOrder = new NotesOnOrder();
				notesOnOrder.setNotesSt(type.getNotesSt());
				notesOnOrder.setUser(securityService.findLoggedInUser());
				notesOnOrder.setPublished(true);
				notesOnOrder.setOrders(bean);
				notesOnOrderRepo.save(notesOnOrder);

				return ResponseEntity.ok(HttpStatus.SC_OK);
			} else {
				return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}
	

	@RequestMapping(value = "all-order-detail/{order_id}/update-order-delivery", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity updateOrderDelivery(@PathVariable Long order_id, @ModelAttribute Orders type) {
		try {
			System.out.println(order_id);
			Orders bean = orderRepo.findOne(order_id);
			if (bean != null) {
				type.getDeliveryManagements().forEach(delivery->{
					DeliveryManagement deliveryManagement=new DeliveryManagement();
					if(delivery.getId()!=null) {
						deliveryManagement=deliveryManagementRepo.findOne(delivery.getId());
						deliveryManagement.setModifiedAt(LocalDateTime.now());
					}else {
						deliveryManagement.setOrders(bean);
						deliveryManagement.setNursery(delivery.getNursery());
					}

					if (delivery.getDeliveryDatest() != null && !delivery.getDeliveryDatest().trim().isEmpty()) {
						String str = delivery.getDeliveryDatest();
						SimpleDateFormat sFormat = new SimpleDateFormat("dd/MM/yyyy");

							try {
								deliveryManagement.setDeliveryDate(sFormat.parse(str));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						

					}
					if (delivery.getDeliveryPerson() != null) {
						deliveryManagement.setDeliveryPerson(delivery.getDeliveryPerson());
					}
					
					deliveryManagementRepo.save(deliveryManagement);

				});
				
				return ResponseEntity.ok(HttpStatus.SC_OK);
			} else {
				return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
