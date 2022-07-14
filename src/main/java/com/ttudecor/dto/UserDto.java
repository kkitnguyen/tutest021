package com.ttudecor.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Integer id;
	private String fullname;
	private String email;
	private String phoneNumber;
	private String address;
	private Date createdDate;
	private Date lastUpdatedDate;
	private boolean isadmin;
}
