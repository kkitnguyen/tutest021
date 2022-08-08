package com.ttudecor.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ttudecor.entity.Category;
import com.ttudecor.entity.OrderDetail;
import com.ttudecor.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	private Integer id;
	private String name;
	private int price;
	private int discountPrice;
	private int quantity;
	private int sold;
	private String image;
	private String description;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private boolean isnew;
	private String url;
	private int categoryId;
}
