package com.ttudecor.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	
	//show all products
	@RequestMapping("/shop")
	public String showAll(Model model, @PageableDefault(size = 9) Pageable pageable) {

		Page<ProductDto> page = productService.getProductDtoPaginated(pageable);

		model.addAttribute("page",page);
		model.addAttribute("categories", categoryService.findAll());
		
		model.addAttribute("shopPage", true);
		return "shop/shop";
	}
	
	//List product filter by category
	@RequestMapping("shop/{categoryUrl}")
	public String showByCategory(Model model, @PathVariable("categoryUrl") String url,
			@PageableDefault(size = 9) Pageable pageable) {
		
		url = url.substring(url.length() - 2);
		int id = Integer.parseInt(url);
		
		Page<ProductDto> page = productService.getProductDtoByCategoryIdPaginated(id, pageable);
		
		Category category = categoryService.findCategoryById(id);
		
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		model.addAttribute("categories", categoryService.findAll());
		
		model.addAttribute("shopPage", true);
		return "shop/shop";
	}
	
	//Search product by name
	@RequestMapping("/search")
	public String search(Model model, @RequestParam("productName") String name,
			@PageableDefault(size = 9) Pageable pageable) {
		
		Page<ProductDto> page = productService.getProductDtoByNamePaginated(name, pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("categories", categoryService.findAll());
		
		System.out.println( "name: "+name);
		model.addAttribute("productName", name);
		model.addAttribute("shopPage", true);
		return "shop/shop";
	}
	
	//Detail product page
	@GetMapping("/product/{url}")
	public String productDetail(Model model, @PathVariable("url") String url,
			@CookieValue(value = "related_product", defaultValue = "") String json,
			HttpServletResponse response) {
		
		url = url.substring(url.length() - 3);
		
		int id = Integer.parseInt(url);
		Product product = productService.findProductById(id);

		List<ProductDto> relatedProducts = productService.getRelatedProducts(response, json, product);
		Category category = product.getCategory();
		
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
