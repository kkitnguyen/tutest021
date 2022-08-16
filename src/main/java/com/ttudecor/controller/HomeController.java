package com.ttudecor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ttudecor.dto.ProductDto;
import com.ttudecor.service.ProductService;

@Controller
public class HomeController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value= {"/index","/", "/home"})
	public String home(ModelMap model) {
		
		List<ProductDto> list = productService.getRandomProductLimitTo(8);
		List<ProductDto> randomList = productService.getRandomProductLimitTo(3);
		List<ProductDto> newestProducts = productService.findNewestProductLimitTo(3);
		List<ProductDto> bestSellerProducts = productService.findBestSellerProductLimitTo(3);
		
		model.addAttribute("products", list);
		model.addAttribute("suggestProducts", randomList);
		model.addAttribute("newestProducts", newestProducts);
		model.addAttribute("bestSellerProducts", bestSellerProducts);
		
		model.addAttribute("homePage", true);
		return "shop/index";
	}
	
	@GetMapping(value= {"/ttu-admin", "/ttu-admin/dashboard"})
	public String dashboardAdmin(Model model) {
		
		model.addAttribute("dashboard", true);
		return "admin/dashboard";
	}
	
	@GetMapping("/about-us")
	public String aboutUs(Model model) {
		
		model.addAttribute("aboutUsPage", true);
		return "shop/about-us";
	}
	
	
}
