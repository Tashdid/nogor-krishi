package com.niil.nogor.krishi.controller;

import java.util.*;
// import java.util.stream.Collectors;

// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/buy/{product}")
    public String buy(@PathVariable Product product, final ModelMap model) {

        List<SaleType> saleTypes = saleTypeRepo.findAll();
        model.addAttribute("saleTypes", saleTypes);
        model.addAttribute("product", product);

        return "site/buy";
    }

    @RequestMapping("/place-order")
    public String placeOrder(final ModelMap model) {

        return "site/place_order";
    }

    @RequestMapping("/order-confirmation/{orderId}")
    public String confirmOrder(@PathVariable String orderId, final ModelMap model) {

        model.addAttribute("orderId", orderId);

        return "site/confirm_order";
    }

}
