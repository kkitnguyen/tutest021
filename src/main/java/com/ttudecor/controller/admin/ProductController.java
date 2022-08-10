package com.ttudecor.controller.admin;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ttudecor.dto.ProductDto;
import com.ttudecor.entity.Category;
import com.ttudecor.entity.Product;
import com.ttudecor.service.CategoryService;
import com.ttudecor.service.ProductService;

@Controller
@RequestMapping("/ttu-admin/product-manager")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	//show list products
	@RequestMapping("")
	public String show(Model model, HttpServletRequest request) {
		model.addAttribute("products", productService.findAll());
		
		model.addAttribute("productManager", true);
		return "admin/list-products";
	}
	
	
	//Add new product
	@GetMapping("add")
	public String add(Model model) {
		
		ProductDto productDto = new ProductDto();
		List<Category> categories = categoryService.findAll();

		model.addAttribute("product", productDto);
		model.addAttribute("categories", categories);
		
		model.addAttribute("productManager", true);
		return "admin/add-product";
	}
	
	@PostMapping("add")
	public String add(Model model, @ModelAttribute("product") ProductDto productDto,
			HttpServletRequest request,
			@RequestParam(name = "imageFile", required = false) MultipartFile image,
			@RequestParam(name = "gallery", required = false) MultipartFile[] gallery ) {
		
		String uploadPath = request.getServletContext().getRealPath("images" + File.separator + "products");
		productService.addProduct(productDto, image, gallery, uploadPath);
		
		return "redirect:";
	}
	
	//update product
	@GetMapping("edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		
		Product product = productService.findProductById(id);
		ProductDto productDto = productService.copy(product);
		
		List<Category> categories = categoryService.findAll();
		
		model.addAttribute("categories", categories);
		model.addAttribute("product", productDto);
		
		model.addAttribute("productManager", true);
		return "admin/edit-product";
	}
	
	@PostMapping("edit")
	public String edit(Model model, @ModelAttribute("product") ProductDto productDto,
			HttpServletRequest request,
			@RequestParam(name = "imageFile", required = false) MultipartFile image,
			@RequestParam(name = "gallery", required = false) MultipartFile[] gallery) {
		
		String uploadPath = request.getServletContext().getRealPath("images" + File.separator + "products");
		productService.updateProduct(productDto, image, gallery, uploadPath);
		
		return "redirect:/ttu-admin/product-manager/edit/" + productDto.getId();
	}
	
	
	//Delete product by id
	@GetMapping("delete/{id}")
	public String delete(Model model, @PathVariable("id") Integer id) {

		productService.deleteById(id);
		return "redirect:/ttu-admin/product-manager";
	}
	
	
}
