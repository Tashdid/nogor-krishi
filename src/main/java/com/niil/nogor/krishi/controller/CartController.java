package com.niil.nogor.krishi.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.httpclient.HttpStatus;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.niil.nogor.krishi.dto.CartDetailsDTO;
import com.niil.nogor.krishi.entity.CartDetails;
import com.niil.nogor.krishi.repo.CartDetailsRepo;
import com.niil.nogor.krishi.repo.NurseryRepo;
import com.niil.nogor.krishi.repo.ProductRepo;
import com.niil.nogor.krishi.repo.SaleTypeRepo;
import com.niil.nogor.krishi.repo.UserRepo;
	
@RestController
@RequestMapping(CartController.URL)
public class CartController extends AbstractController{
	static final String URL = "/test/cart";

	@Autowired ProductRepo productRepo;
	@Autowired SaleTypeRepo saleTypeRepo;
	@Autowired CartDetailsRepo cartDetailsRepo;
	@Autowired UserRepo userRepo;
	@Autowired NurseryRepo nurseryRepo;
	
	@RequestMapping(value="/add-to-cart/",method=RequestMethod.POST)
	public ResponseEntity addToCart(@RequestBody CartDetailsDTO cartDetailsDTO,HttpSession httpSession) {
		
		try{
			System.out.println("cartId1 " + httpSession.getAttribute("cartId"));
			
			if(httpSession.getAttribute("cartId") == null){
				 
				 httpSession.setAttribute("cartId", RequestContextHolder.currentRequestAttributes().getSessionId());
					
			 }
			
			CartDetails cartDetails = new CartDetails();
			cartDetails.setSessionId((String)httpSession.getAttribute("cartId"));
			cartDetails.setProduct(productRepo.getOne(cartDetailsDTO.getProduct_id()));
			cartDetails.setSaleType(saleTypeRepo.getOne(cartDetailsDTO.getSale_type_id()));
			cartDetails.setQuantity(cartDetailsDTO.getQuantity());
			cartDetails.setUnit_price(cartDetailsDTO.getUnit_price());
			cartDetails.setNursery(nurseryRepo.getOne(cartDetailsDTO.getNursery_id()));
			
			httpSession.setAttribute("cartItem", cartDetails);
			System.out.println("cartId2 " + httpSession.getAttribute("cartId"));
			
			cartDetailsRepo.save(cartDetails);
			return ResponseEntity.ok(HttpStatus.SC_OK);
		}
		catch(HibernateException e){
			e.printStackTrace();
			return ResponseEntity.ok(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value="/cart-details/",method=RequestMethod.GET)
	public List<CartDetails> getCartDetails(HttpSession httpSession) {

		if(httpSession.getAttribute("cartId") == null){
			return null;
		}
		List<CartDetails> cartDetailsList = cartDetailsRepo.findAllBysessionId((String)httpSession.getAttribute("cartId"));
		 
		if(!cartDetailsList.isEmpty()){
			return cartDetailsList;
		}
		return cartDetailsList;
	}
}
