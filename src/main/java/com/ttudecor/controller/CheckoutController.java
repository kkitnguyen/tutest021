package com.ttudecor.controller;

import java.util.Collection;

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

import com.ttudecor.dto.CartItemDto;
import com.ttudecor.dto.OrderDto;
import com.ttudecor.entity.User;
import com.ttudecor.service.CartService;
import com.ttudecor.service.OrderService;
import com.ttudecor.service.UserService;

@Controller
public class CheckoutController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;

	@GetMapping("/checkout")
	public String checkout(Model model,
			@CookieValue(value = "cart", defaultValue = "") String cartJson) {
		
		cartService.getCartFromCookie(cartJson);
		
		Collection<CartItemDto> list = cartService.getCartItems();
		
		if(cartService.count() > 0) {
			//return to checkout page if cart is not empty
			
			OrderDto orderDto = new OrderDto();
			
			//pre-filled customer information if customer logged in
			if(session.getAttribute("fullname") != null) {
				int userId = (int) session.getAttribute("userId");
				User user = userService.findUserById(userId);

				orderDto.setFullname(user.getFullname());
				orderDto.setEmail(user.getEmail());
				orderDto.setPhoneNumber(user.getPhoneNumber());
				orderDto.setAddress(user.getAddress());
			}
			
			model.addAttribute("order", orderDto);
			model.addAttribute("cartItems", list);
			model.addAttribute("cartAmount", cartService.getAmount());
			
			return "shop/checkout";
			
			//redirect to cart page if cart is empty
		} else return "redirect:/cart";
		
	}
	
	@PostMapping("/checkout")
	public String checkout(Model model,
			@CookieValue(value = "cart", defaultValue = "") String cartJson,
			@ModelAttribute("order") OrderDto orderDto,
			HttpServletResponse response) {

		cartService.getCartFromCookie(cartJson);
		Collection<CartItemDto> listCart = cartService.getCartItems();
		
		if(cartService.count() > 0) {
			//checkout if cart is not empty
			orderService.checkout(listCart, orderDto);
			
			Cookie cookie = new Cookie("cart", "");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			
			model.addAttribute("success", true);
			return "shop/checkout";
			
			//redirect to cart page if cart is empty
		} else return "redirect:/cart";
		
	}
}
