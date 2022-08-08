package com.ttudecor.entity;

import java.time.LocalDateTime;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	@Column(length = 14, nullable = false)
	private String billCode;
	
	@Column(length = 50, nullable = false)
	private String fullname;
	
	@Column(length = 100, nullable = false)
	private String email;
	
	@Column(length = 11, nullable = false)
	private String phoneNumber;
	
	@Column(length = 200, nullable = false)
	private String address;
	
	@Column(length = 200)
	private String note;

	private LocalDateTime orderTime;
	
	@Column(nullable = false)
	private int status;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;
	
}
