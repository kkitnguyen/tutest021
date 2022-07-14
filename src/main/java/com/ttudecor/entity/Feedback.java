package com.ttudecor.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 50)
	private String fullname;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	@Column(nullable = false, length = 11)
	private String phoneNumber;
	
	@Column(length = 200)
	private String message;
	
	@Temporal(TemporalType.DATE)
	private Date createdDate;
}
