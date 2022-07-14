package com.ttudecor.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.ttudecor.entity.Gallery;
import com.ttudecor.entity.Product;
import com.ttudecor.service.CategoryService;
import com.ttudecor.service.ProductService;
import com.ttudecor.utils.StringFormatUtils;
import com.ttudecor.utils.UploadUtils;

@Controller
@RequestMapping("/ttu-admin/product-manager")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("")
	public String show(Model model, HttpServletRequest request) {
		model.addAttribute("products", productService.findAll());
		
		model.addAttribute("productManager", true);
		return "admin/list-products";
	}
	
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
			@RequestParam("imageFile") MultipartFile image,
			@RequestParam(name = "gallery", required = false) MultipartFile[] gallery ) {
		
		String uploadPath = request.getServletContext().getRealPath("images\\products");
		productService.addProduct(productDto, image, gallery, uploadPath);
		
		return "redirect:";
	}
	
	@GetMapping("edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		
		Optional<Product> opt = productService.findById(id);
		Product product = new Product();
		if(opt != null) product = opt.get();
		
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
		
		String uploadPath = request.getServletContext().getRealPath("images\\products");
		productService.updateProduct(productDto, image, gallery, uploadPath);
		
		return "redirect:/ttu-admin/product-manager/edit/" + productDto.getId();
	}
	
	@GetMapping("delete/{id}")
	public String delete(Model model, @PathVariable("id") Integer id) {
		Optional<Product> opt = productService.findById(id);
		Product product = new Product();
		if(opt != null) product = opt.get();

		productService.delete(product);
		return "redirect:/ttu-admin/product-manager";
	}
	
	
}
