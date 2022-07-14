package com.ttudecor.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ttudecor.dto.CategoryDto;
import com.ttudecor.entity.Category;
import com.ttudecor.repository.ProductRepository;
import com.ttudecor.service.CategoryService;
import com.ttudecor.utils.StringFormatUtils;

@Controller
@RequestMapping("/ttu-admin/category-manager")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private StringFormatUtils stringFormatUtils;
	
	@RequestMapping("")
	public String show(Model model) {
		
		model.addAttribute("categories", categoryService.getAllCategoryDto());
		
		model.addAttribute("category", new Category());
		
		model.addAttribute("categoryManager", true);
		return "admin/list-categories";
	}
	
	@PostMapping("add")
	public String add(Model model, @ModelAttribute("category") Category category) {
		
		String nameFormat = stringFormatUtils.convertToUrlFomart(category.getName());
		String idFormat = "c" + String.format("%02d", category.getId());
		String url = nameFormat + "-" + idFormat;
		
		category.setUrl(url);
		
		categoryService.save(category);
		model.addAttribute("message", "Thêm danh mục thành công!");
		
		model.addAttribute("categoryManager", true);
		return "admin/list-categories";
	}
	
	@GetMapping("edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		Optional<Category> opt = categoryService.findById(id);
		Category category = new Category();
		if(opt != null) category = opt.get();

		model.addAttribute("category", category);
		model.addAttribute("categoryManager", true);
		return "admin/edit-category";
	}
	
	@PostMapping("edit")
	public String update(Model model, @ModelAttribute("category") Category category) {
		
		String nameFormat = stringFormatUtils.convertToUrlFomart(category.getName());
		String idFormat = "c" + String.format("%02d", category.getId());
		String url = nameFormat + "-" + idFormat;
		
		category.setUrl(url);
		
		categoryService.save(category);
		model.addAttribute("message", "Cập nhật danh mục thành công!");
		model.addAttribute("category", category);
		
		model.addAttribute("categoryManager", true);
		return "admin/edit-category";
	}
	
	@GetMapping("delete/{id}")
	public String delete(Model model, @PathVariable("id") Integer id) {
		Optional<Category> opt = categoryService.findById(id);
		Category category = new Category();
		if(opt != null) category = opt.get();
		
		
		categoryService.delete(category);
		return "redirect:/ttu-admin/category-manager";
	}
}
