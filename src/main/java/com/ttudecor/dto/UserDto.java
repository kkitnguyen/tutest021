package com.ttudecor.dto;

import java.time.LocalDateTime;

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
	private LocalDateTime createdTime;
	private boolean isadmin;
}
