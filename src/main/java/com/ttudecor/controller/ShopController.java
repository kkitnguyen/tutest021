package com.ttudecor.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.dto.ProductDto;
import com.ttudecor.entity.Category;
import com.ttudecor.entity.Product;
import com.ttudecor.service.CategoryService;
import com.ttudecor.service.ProductService;

@Controller
public class ShopController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("/shop")
	public String showAll(Model model) {
		model.addAttribute("products", productService.getAllProductDto());
		model.addAttribute("categories", categoryService.findAll());
		
		model.addAttribute("shopPage", true);
		return "shop/shop";
	}
	
	@RequestMapping("/search")
	public String search(Model model, @RequestParam("productName") String name) {
		model.addAttribute("products", productService.getProductDtoByName(name));
		model.addAttribute("categories", categoryService.findAll());
		
		model.addAttribute("shopPage", true);
		return "shop/shop";
	}
	
	@RequestMapping("shop/{url}")
	public String showByCategory(Model model, @PathVariable("url") String url) {
		
		url = url.substring(url.length() - 2);
		int id = Integer.parseInt(url);
		
		Optional<Category> opt = categoryService.findById(id);
		Category category = new Category();
		if(opt != null) category = opt.get();
		
		model.addAttribute("products", category.getProducts());
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("category", category);
		
		model.addAttribute("shopPage", true);
		return "shop/shop";
	}
	
	@GetMapping("/product/{url}")
	public String productDetail(Model model, @PathVariable("url") String url,
			@CookieValue(value = "related_product", defaultValue = "") String json,
			HttpServletResponse response) {
		
		url = url.substring(url.length() - 3);
		
		int id = Integer.parseInt(url);
		
		Optional<Product> opt = productService.findById(id);
		Product product = new Product();
		if(opt != null) product = opt.get();
		
		Category category = product.getCategory();
		
		List<ProductDto> relatedProducts = productService.getRelatedProducts(response, json, product);
		
		
		model.addAttribute("category", category);
		model.addAttribute("product", product);
		model.addAttribute("relatedProducts", relatedProducts);
		
		model.addAttribute("shopPage", true);
		return "shop/product-detail";
	}
	
	@GetMapping("/product")
	public String product() {
		return "redirect:/shop";
	}
}
