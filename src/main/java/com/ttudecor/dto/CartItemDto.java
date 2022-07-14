package com.ttudecor.dto;

import com.ttudecor.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
	private int productId;
	private String productName;
	private String image;
	private int price;
	private int quantity;
}
