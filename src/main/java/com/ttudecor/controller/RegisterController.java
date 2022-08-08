package com.ttudecor.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.dto.UserDto;
import com.ttudecor.service.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/register")
	public String register(Model model){
		
		//redirect to home page when logged in
		if(session.getAttribute("fullname") != null) 
			return "redirect:/home";
		else {
			UserDto userDto = new UserDto();
			model.addAttribute("user", userDto);
			
			return "shop/register";
		}
	}
	
	@PostMapping("/register")
	public String login(Model model, @ModelAttribute("user") UserDto userDto,
			@RequestParam("password") String password, @RequestParam("rePassword") String rePassword){
		
		userDto.setIsadmin(false);
		boolean success = userService.register(model, userDto, password, rePassword);
		
		if(success) model.addAttribute("success", true);
		return "shop/register";
	}
	
}
