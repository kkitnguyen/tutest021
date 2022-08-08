package com.ttudecor.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 100, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int price;
	
	private int discountPrice;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = false)
	private int sold;
	
	@Column(length = 200)
	private String image;
	
	@Column(nullable = false)
	private String description;

	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	
	@Column(nullable = false, length = 100)
	private String url;
	
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Gallery> gallery;
	
	
}
