package com.ttudecor.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ttudecor.dto.CartItemDto;
import com.ttudecor.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@GetMapping("")
	public String showAll(Model model,
			@CookieValue(value = "cart", defaultValue = "") String cartJson,
			HttpServletResponse response) {

		cartService.getCartFromCookie(cartJson);
		cartService.addCartToCookie(response);
		
		Collection<CartItemDto> list = cartService.getCartItems();
		
		model.addAttribute("cartItems", list);
		model.addAttribute("cartAmount", cartService.getAmount());
		return "shop/shop-cart";
	}
	
	@GetMapping("add/{url}")
	public String add(@PathVariable("url") String url,
			@CookieValue(value = "cart", defaultValue = "") String cartJson,
			@RequestParam(name = "quantity", required = false, defaultValue = "1") int quantity,
			HttpServletResponse response) {
		
		url = url.substring(url.length() - 3);
		int productId = Integer.parseInt(url);
		
		cartService.getCartFromCookie(cartJson);
		cartService.add(productId, quantity);
		
		cartService.addCartToCookie(response);
		
		return "redirect:/cart";
	}
	
	@GetMapping("remove/{productId}")
	public String remove(@PathVariable("productId") int productId,
			@CookieValue(value = "cart", defaultValue = "") String cartJson,
			HttpServletResponse response) {
		cartService.getCartFromCookie(cartJson);

		cartService.remove(productId);
		cartService.addCartToCookie(response);

		return "redirect:/cart";
	}
	
	@PostMapping("update")
	public String remove(@RequestParam("productId") int productId,
			@RequestParam("quantity") int quantity,
			@CookieValue(value = "cart", defaultValue = "") String cartJson,
			HttpServletResponse response) {
		
		cartService.getCartFromCookie(cartJson);

		cartService.update(productId, quantity);
		cartService.addCartToCookie(response);

		return "redirect:/cart";
	}
	
	@GetMapping("amount")
	@ResponseBody
	public Integer amount(@CookieValue(value = "cart", defaultValue = "") String cartJson){
		cartService.getCartFromCookie(cartJson);
		
		return cartService.count();
	}
	
	
}
