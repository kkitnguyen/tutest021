package com.ttudecor.dto;

import com.ttudecor.entity.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemDto {
	private int productId;
	private String productName;
	private String image;
	private String url;
	private int price;
	private int quantity;
	
	public CartItemDto(Product product, int quantity) {
		this.productId = product.getId();
		this.productName = product.getName();
		this.image = product.getImage();
		this.url = product.getUrl();

		this.quantity = quantity;
		
		//use discount price if discount price > 0
		if(product.getDiscountPrice() > 0) 
			this.price = product.getDiscountPrice();
		else
			this.price = product.getPrice();
		
	}
	
	
}
