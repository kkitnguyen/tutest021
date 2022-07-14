package com.ttudecor.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.dto.UserDto;
import com.ttudecor.entity.User;
import com.ttudecor.service.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/register")
	public String register(Model model){
		
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
		
		User user = new User();
		user = userService.findByEmail(userDto.getEmail());
		
		if(user != null)
			model.addAttribute("message", "Email đã tồn tại, vui lòng chọn email khác.");
		else if(!password.equals(rePassword))
			model.addAttribute("message", "Nhập lại mật khẩu không khớp.");
		else if(password.length() < 6)
			model.addAttribute("message", "Mật khẩu tối thiểu 6 ký tự.");
		else {
			user = new User();
			user = userService.copy(userDto);
			
			Date now = new Date();
			user.setCreatedDate(now);
			user.setPassword(password);
			user.setIsadmin(false);
			userService.save(user);;;
			
			model.addAttribute("success", true);
		}
		return "shop/register";
	}
	
}
