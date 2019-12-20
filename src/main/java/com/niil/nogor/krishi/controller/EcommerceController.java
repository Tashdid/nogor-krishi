package com.niil.nogor.krishi.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpStatus;

// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
// import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.niil.nogor.krishi.config.Constants;
import com.niil.nogor.krishi.entity.*;
import com.niil.nogor.krishi.repo.*;
// import com.niil.nogor.krishi.service.SecurityService;
// import com.niil.nogor.krishi.view.LArea;
// import com.niil.nogor.krishi.view.LNursery;
// import com.niil.nogor.krishi.view.LProduct;
// import com.niil.nogor.krishi.view.LayoutRQ;
import com.niil.nogor.krishi.service.SecurityService;

/**
 * @author Himel
 * @email sabhimel@gmail.com
 * @since Oct 11, 2019
 *
 */
@Controller
@RequestMapping
public class EcommerceController extends AbstractController {


    //	@Autowired ProductTypeRepo productTypeRepo;
//	@Autowired ProductRepo productRepo;
//	@Autowired ProductPriceRepo productPriceRepo;
//	@Autowired NurseryRepo nurseryRepo;
//	@Autowired AreaRepo areaRepo;
//	@Autowired GardenLayoutRepo gardenLayoutRepo;
//	@Autowired GardenBlockRepo gardenBlockRepo;
//	@Autowired MaterialPriceRepo materialPriceRepo;
//	@Autowired SecurityService securityService;
//	@Autowired GalleryImagesRepo galleryImagesRepo;
//	@Autowired CarouselImagesRepo carouselImagesRepo;
//	@Autowired GardenDesignImagesRepo gardenDesignImagesRepo;
//	@Autowired MaterialRepo materialRepo;
    @Autowired SaleTypeRepo saleTypeRepo;
//	@Autowired private APIContentRepo contentRepo;
    @Autowired CartDetailsRepo cartDetailsRepo;
    @Autowired
	ProductPriceRepo priceRepo;
	@Autowired
	ProductRepo productRepo;
	@Autowired
	ProductPropertyValueRepo productPropertyValueRepo;
	@Autowired
	ProductPriceOnPropertyValueRepo productPriceOnPropertyValueRepo;
	@Autowired
	ProductPropertyMappingRepo productPropertyMappingRepo;
	@Autowired
	DemographicDataRepo demographicDataRepo;

	@Autowired SecurityService securityService;
	@Autowired UserAddressPreferenceRepo userAddressPreferenceRepo;

    @RequestMapping("/buy/{product}")
    public String buy(@PathVariable Product product, final ModelMap model) {

        //List<SaleType> saleTypes = saleTypeRepo.findAll();
        //model.addAttribute("saleTypes", saleTypes);
    	
    	List<ProductPropertyMapping> propertyMappingList = productPropertyMappingRepo.findAllByProduct(product);
		Map<String, List<ProductPropertyValue>> mapProperty = new HashMap<String, List<ProductPropertyValue>>();
		propertyMappingList.forEach(productPropertyMapping -> {
			List<ProductPropertyValue> propertyValueList = productPropertyValueRepo
					.findAllByProductProperty(productPropertyMapping.getProductProperty());

			mapProperty.put(productPropertyMapping.getProductProperty().getName(), propertyValueList);
			
		});

		model.addAttribute("bivaggulo", demographicDataRepo.findAllByParentIdIsNullOrderByNameAsc());
		model.addAttribute("onnannogulo", demographicDataRepo.findAllByParentIdIsNotNull().stream()
				  .collect(Collectors.groupingBy(DemographicData::getParentId)));
		
		
        model.addAttribute("mapProperty", mapProperty);
        model.addAttribute("product", product);

        return "site/buy";
    }

    @RequestMapping("/place-order")
    public String placeOrder(HttpSession httpSession, final ModelMap model) {
    	
    	if(httpSession.getAttribute("cartId") == null){
			return null; //@todo return error message
		 }
		
		
		List<CartDetails> cartDetailsList = cartDetailsRepo.findAllBysessionId(
				(String)httpSession.getAttribute("cartId"));
		
		if(cartDetailsList.size() == 0) {
			return null; // @todo return error message
		}
		
		BigDecimal totalPrice = new BigDecimal(0);
		for(int i=0;i<cartDetailsList.size();i++){
			CartDetails cartDetail = cartDetailsList.get(i);
			totalPrice = totalPrice.add(
					cartDetail.getUnit_price().multiply( 
							new BigDecimal(cartDetail.getQuantity())
							)
					);
			
		}
		model.addAttribute("totalPrice", totalPrice);
		
		
//		String cart = (String) httpSession.getAttribute("cartId");

//		cartDetailsList.forEach(cartDl -> {
//			cartDl.getProductPrice().getProductPriceOnPropertyValueList().forEach(pp -> {
//				pp.setProductPrice(null);
//			});
//		});
				
		model.addAttribute("addedProducts", cartDetailsList);
		
		long nurseryCount = cartDetailsList.stream().map(CartDetails::getProductPrice).map(ProductPrice::getNursery).distinct().count();
		Settings settings = settingsRepo.findAll().stream().findFirst().orElse(new Settings());
		
		User loggedUser=securityService.findLoggedInUser();
		if(loggedUser!=null) {
			model.addAttribute("deliveryAddressList", userAddressPreferenceRepo.findAllByUserAndAddressType(loggedUser,AddressType.DELIVER));
			model.addAttribute("billingAddressList", userAddressPreferenceRepo.findAllByUserAndAddressType(loggedUser,AddressType.BILLING));
		}
		
		model.addAttribute("districtList", demographicDataRepo.findAllByTypeOrderByNameAsc(new Byte("1")));
		model.addAttribute("user", loggedUser!=null?loggedUser:new User());
		model.addAttribute("deliverycharge", settings.getDeliveryCharge());
		model.addAttribute("totalDeliverycharge", nurseryCount * settings.getDeliveryCharge());
		model.addAttribute("total", BigDecimal.valueOf( nurseryCount * settings.getDeliveryCharge() ).add(totalPrice));
		model.addAttribute("nurseryCount", nurseryCount);

        return "site/place_order";
    }

    @RequestMapping("/order-confirmation/{orderId}")
    public String confirmOrder(@PathVariable String orderId, final ModelMap model) {

        model.addAttribute("orderId", orderId);

        return "site/confirm_order";
    }



	@RequestMapping(value = "/get-demographic-data-by-parent/{parentId}", method = RequestMethod.GET)
	@ResponseBody
	public List<DemographicData> getDemographicDataByParent(@PathVariable Long parentId) {
			return demographicDataRepo.findAllByParentIdOrderByNameAsc(parentId);	
	}


	@RequestMapping(value = "/add-user-address-preference", method = RequestMethod.POST)
	@ResponseBody
	public UserAddressPreference addUserAddressPreference(@RequestBody UserAddressPreference userAddressPreference) {
		
		User loggedUser=securityService.findLoggedInUser();
		userAddressPreference.setUser(loggedUser);	
		userAddressPreferenceRepo.save(userAddressPreference);
		return userAddressPreference;	
	}

}
