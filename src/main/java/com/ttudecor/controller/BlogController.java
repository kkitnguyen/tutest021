package com.ttudecor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BlogController {
	//show all blogs
	@RequestMapping("/blog")
	public String showAll(Model model){
		
		
		model.addAttribute("blogPage", true);
		return "shop/blog";
	}
	
	//show blog detail
	@RequestMapping("/blog/{url}")
	public String blogDetail(Model model, @PathVariable("url") String url){
		
		
		model.addAttribute("blogPage", true);
		return "shop/blog-detail";
	}
}
