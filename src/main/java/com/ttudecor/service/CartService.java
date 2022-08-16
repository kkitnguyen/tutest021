package com.ttudecor.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttudecor.dto.CartItemDto;
import com.ttudecor.entity.Product;
import com.ttudecor.model.CartItemModel;
import com.ttudecor.utils.EncryptionUtils;


@Service
public class CartService {
	
	private Map<Integer, CartItemDto> cart = new HashMap<Integer, CartItemDto>();

	private final ProductService productService;
	
	@Autowired
	public CartService(ProductService productService) {
		this.productService = productService;
	}
	
	//Get cart from cookie
	public void getCartFromCookie(String cartJson) {
		cartJson = EncryptionUtils.dencrypt("KEYKEYKEY", cartJson);

		Gson gson = new Gson();
		Type type = new TypeToken<Collection<CartItemModel>>(){}.getType();
	
		Collection<CartItemModel> list = gson.fromJson(cartJson, type);
		Product product = new Product();
		
		cart = new HashMap<Integer, CartItemDto>();

		if(list != null) {
			for(CartItemModel item : list) {
				product = productService.findProductById(item.getProductId());
				
				if(product != null) {
					CartItemDto itemDto = new CartItemDto();

					if(product.getQuantity() > 0 ) {

						//cart quantity must be <= product quantity
						int quantity;
						if(item.getQuantity() > product.getQuantity()) quantity = product.getQuantity();
						else quantity = item.getQuantity();
						
						itemDto = new CartItemDto(product, quantity);
						cart.put(product.getId(), itemDto);
					}
				}
				
			}
		}
	}
	
	//add cart to cookie
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
		
	//add a quantity to cart
	public void add(int productId, int quantity) {
		Product product = productService.findProductById(productId);
		
		CartItemDto item = cart.get(productId);
		
		if(item != null) {
			int newQuantity = item.getQuantity() + quantity;
			
			if(newQuantity <= product.getQuantity()) { 
				item.setQuantity(newQuantity);
			}else item.setQuantity(product.getQuantity());
			
		}else {
			//add to cart if product in stock
			if(product.getQuantity() >0 ) {
				item = new CartItemDto();

				if(quantity > product.getQuantity()) 
					quantity = product.getQuantity();
				
				item = new CartItemDto(product, quantity);
				cart.put(product.getId(), item);
			}
		}
	}
	
	//update quantity
	public void update(int productId, int quantity) {
		CartItemDto item = cart.get(productId);

		if(quantity <=0)
			cart.remove(productId);
		else {
			Product product = productService.findProductById(productId);
			
			if(quantity <= product.getQuantity())
				item.setQuantity(quantity);
			else item.setQuantity(product.getQuantity());
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
	
	//get amount of product in cart
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
