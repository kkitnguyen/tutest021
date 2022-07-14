package com.ttudecor.controller;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.dto.UserDto;
import com.ttudecor.entity.User;
import com.ttudecor.service.UserService;
import com.ttudecor.utils.EncryptionUtils;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/login")
	public String login(Model model,
			@CookieValue(name="uid", defaultValue = "") String uid){

		if(session.getAttribute("fullname") != null) 
			return "redirect:/home";
		else {
			return "shop/login";
		}
			
	}
	
	@PostMapping("/login")
	public String login(Model model, @RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam(name = "remember", required = false) boolean remember, 
			HttpServletResponse response){
		
		User user = userService.findByEmailAndPassword(email, password);
		
		Object uri = session.getAttribute("redirectUri");
		
		if(user != null) {
			
			if(remember) {
				String uid = EncryptionUtils.encrypt("LoginID", user.getId() + "");
				Cookie cookie = new Cookie("uid", uid);
				cookie.setMaxAge(60*60*24 * 15);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			
			session.setAttribute("fullname", user.getFullname());
			session.setAttribute("userId", user.getId());
			
			if(user.isIsadmin()) session.setAttribute("admin", true);
			
			if(uri != null) {
				session.removeAttribute("redirectUri");
				return "redirect:" + uri;
			}

			if(user.isIsadmin()) return "redirect:/ttu-admin";
			else return "redirect:/home";
		}
		else {
			model.addAttribute("message", "Sai email hoặc mật khẩu");
			return "shop/login";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletResponse response){
		Cookie cookie = new Cookie("uid", "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		session.invalidate();
		return "redirect:/home";
	}
}
