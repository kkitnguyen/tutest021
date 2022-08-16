package com.ttudecor.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ttudecor.entity.Category;
import com.ttudecor.service.CategoryService;

@Controller
@RequestMapping("/ttu-admin/category-manager")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("")
	public String show(Model model) {
		
		model.addAttribute("categories", categoryService.getAllCategoryDto());
		
		model.addAttribute("category", new Category());
		
		model.addAttribute("categoryManager", true);
		return "admin/list-categories";
	}
	
	//Add new category
	@PostMapping("add")
	public String add(Model model, @ModelAttribute("category") Category category) {
		if(categoryService.AddOrUpdateCategory(category))
			model.addAttribute("success", "Thêm danh mục thành công!");
		else model.addAttribute("message", "Có lỗi xảy ra.");
		
		model.addAttribute("categories", categoryService.getAllCategoryDto());
		model.addAttribute("category", new Category());
		model.addAttribute("categoryManager", true);

		return "admin/list-categories";
	}
	
	//Edit category name
	@GetMapping("edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		Category category = categoryService.findCategoryById(id);
		
		model.addAttribute("category", category);
		model.addAttribute("categoryManager", true);
		return "admin/edit-category";
	}
	
	@PostMapping("edit")
	public String update(Model model, @ModelAttribute("category") Category category) {
		
		if(categoryService.AddOrUpdateCategory(category))
			model.addAttribute("success", "Cập nhật danh mục thành công!");
		else model.addAttribute("message", "Có lỗi xảy ra.");

		model.addAttribute("category", category);
		model.addAttribute("categoryManager", true);
		return "admin/edit-category";
	}
	
	//Delete category by id
	@GetMapping("delete/{id}")
	public String delete(Model model, @PathVariable("id") Integer id) {
		try {
			categoryService.deleteById(id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/ttu-admin/category-manager";
	}
}
