package com.ttudecor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttudecor.entity.OrderDetail;
import com.ttudecor.repository.OrderDetailRepository;

@Service
public class OrderDetailService {
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	public OrderDetailService(OrderDetailRepository orderDetailRepository) {
		this.orderDetailRepository = orderDetailRepository;
	}

	public <S extends OrderDetail> S save(S entity) {
		return orderDetailRepository.save(entity);
	}

	public List<OrderDetail> findAll() {
		return orderDetailRepository.findAll();
	}

	public Optional<OrderDetail> findById(Integer id) {
		return orderDetailRepository.findById(id);
	}

	public void delete(OrderDetail entity) {
		orderDetailRepository.delete(entity);
	}
	
	
}
