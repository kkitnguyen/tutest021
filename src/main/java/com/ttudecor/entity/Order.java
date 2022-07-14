package com.ttudecor.entity;

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
	
	@Temporal(TemporalType.DATE)
	private Date orderTime;
	
	@Column(nullable = false)
	private int status;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;

	public Order(String billCode, String fullname, String email, String phoneNumber, String address, String note,
			Date orderTime, int status) {
		super();
		this.billCode = billCode;
		this.fullname = fullname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.note = note;
		this.orderTime = orderTime;
		this.status = status;
	}
	
	
	
}
