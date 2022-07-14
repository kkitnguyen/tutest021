package com.ttudecor.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

	private Integer id;
	private int userId;
	private String billCode;
	private String fullname;
	private String email;
	private String phoneNumber;
	private String address;
	private String note;
	private Date orderTime;
	private int status;
	private int amount;

}
