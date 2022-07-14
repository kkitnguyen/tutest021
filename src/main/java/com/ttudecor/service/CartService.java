package com.ttudecor.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttudecor.dto.CartItemDto;
import com.ttudecor.entity.Product;
import com.ttudecor.model.CartItemModel;
import com.ttudecor.utils.EncryptionUtils;


@Service
@SessionScope
public class CartService {
	
	private Map<Integer, CartItemDto> cart = new HashMap<Integer, CartItemDto>();
	
	
	
	public Map<Integer, CartItemDto> getCart() {
		return cart;
	}

	public void setCart(Map<Integer, CartItemDto> cart) {
		this.cart = cart;
	}

	ProductService productService;
	
	@Autowired
	public CartService(ProductService productService) {
		this.productService = productService;
	}
	
	public void getCartFromCookie(String cartJson) {
		cartJson = EncryptionUtils.dencrypt("KEYKEYKEY", cartJson);
		
		cart = new HashMap<Integer, CartItemDto>();
		
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<CartItemModel>>(){}.getType();
	
		
		Collection<CartItemModel> list = gson.fromJson(cartJson, type);
		Product product = new Product();
		
		if(list != null) {
			for(CartItemModel item : list) {
				product = productService.findById(item.getProductId()).get();
				
				if(product != null) {
					CartItemDto itemDto = new CartItemDto();
					
					if(product.getDiscountPrice() == 0) 
						itemDto = new CartItemDto(product.getId(), 
							product.getName(), product.getImage(), product.getPrice(), item.getQuantity());
					else
						itemDto = new CartItemDto(product.getId(), 
							product.getName(), product.getImage(), product.getDiscountPrice(), item.getQuantity());
							
					cart.put(product.getId(), itemDto);
				}
				
			}
		}
	}
		
	public void addCartToCookie(HttpServletResponse response) {
		Collection<CartItemDto> list = null;
		
		if(cart != null) list = cart.values();
		Collection<CartItemModel> list2 = new ArrayList<CartItemModel>();
		
		String json = null;
		
		if(list != null) {
			for(CartItemDto item : list) {
				list2.add(new CartItemModel(item.getProductId(), item.getQuantity()));
			}
			
			Gson gson = new Gson();
			json = gson.toJson(list2);
		}
		
		json = EncryptionUtils.encrypt("KEYKEYKEY", json);
		
		Cookie cookie = new Cookie("cart", json);
		cookie.setMaxAge(30 * 24 * 60  * 60);
		cookie.setPath("/");
		
		response.addCookie(cookie);
	}
		
	public void add(int productId) {
		
		Optional<Product> opt = productService.findById(productId);
		Product product = opt.get();

		CartItemDto oldItem = cart.get(productId);
		
		if(oldItem != null) {
			oldItem.setQuantity(oldItem.getQuantity() + 1);
			cart.replace(productId, oldItem);
			
		}else {
			CartItemDto item = new CartItemDto();
			
			if(product.getDiscountPrice() == 0) 
				item = new CartItemDto(product.getId(), 
					product.getName(), product.getImage(), product.getPrice(), 1);
			else
				item = new CartItemDto(product.getId(), 
					product.getName(), product.getImage(), product.getDiscountPrice(), 1);
			
			cart.put(product.getId(), item);
		}
	}
	
	public void remove(int productId) {
		cart.remove(productId);
	}
	
	
	public Collection<CartItemDto> getCartItems(){
		if(cart != null) return cart.values();
		
		return null;
	}
	
	public void clear() {
		cart.clear();
	}
	
	public void update(int productId, int quantity) {
		CartItemDto item = cart.get(productId);
		
		if(quantity <=0)
			cart.remove(productId);
		else item.setQuantity(quantity);
	}
	
	public int getAmount() {
		Collection<CartItemDto> list = null;
		
		int amount = 0;
		if(cart != null) {
			list = cart.values();
			
			for(CartItemDto item : list) {
				amount += item.getQuantity() * item.getPrice();
			}
		}
		
		return amount;
	}
	
//	public int getAmount() {
//		return cart.values().stream().mapToInt(item -> item.getQuantity() * item.getPrice()).sum();
//	}
	
	
	public int count() {
		if(cart.isEmpty()) return 0;
		
		return cart.size();
	}
	
}
