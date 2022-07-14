package com.ttudecor.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttudecor.dto.CartItemDto;
import com.ttudecor.dto.OrderDto;
import com.ttudecor.entity.Order;
import com.ttudecor.entity.OrderDetail;
import com.ttudecor.entity.Product;
import com.ttudecor.entity.User;
import com.ttudecor.repository.OrderRepository;

@Service
public class OrderService {
	OrderRepository orderRepository;
	ProductService productService;
	OrderDetailService orderDetailService;
	
	@Autowired
	
	public OrderService(OrderRepository orderRepository, ProductService productService,
			OrderDetailService orderDetailService) {
		this.orderRepository = orderRepository;
		this.productService = productService;
		this.orderDetailService = orderDetailService;
	}

	
	public void checkout(Collection<CartItemDto> listCart, OrderDto orderDto) {
		
		Date now = new Date();
		String billCode = createBillCode(now);
		
		Order order = new Order(billCode, orderDto.getFullname(), orderDto.getEmail(), 
				orderDto.getPhoneNumber(), orderDto.getAddress(), orderDto.getNote(), now, 0);
		
		if(orderDto.getUserId() != 0) {
			User user = new User();
			user.setId(orderDto.getUserId());
			order.setUser(user);
		}

		save(order);
		
		for(CartItemDto item : listCart) {
			Product product = productService.findById(item.getProductId()).get();
			product.setSold(product.getSold() + item.getQuantity());
			productService.save(product);
			OrderDetail detail = new OrderDetail(order, product, item.getPrice(), item.getQuantity());
			
			orderDetailService.save(detail);
		}
	}
	
	public String createBillCode(Date now) {

		String year = (now.getYear() + "").substring(1);
		
		String month = "";
		if(now.getMonth() >= 9) month = now.getMonth() + 1 + "";
		else month = "0" + (now.getMonth() + 1);
		
		String date = "";
		if(now.getDate()>=10) date = now.getDate() + "";
		else date = "0" + now.getDate();
		
		String hour = "";
		if(now.getHours()>=10) hour = now.getHours() + "";
		else hour = "0" + now.getHours();
		
		String minute = "";
		if(now.getMinutes()>=10) minute = now.getMinutes() + "";
		else minute = "0" + now.getMinutes();
		
		String second = "";
		if(now.getSeconds()>=10) second = now.getSeconds() + "";
		else second = "0" + now.getSeconds();
		
		String billCode = "TU" + year + month + date + hour + minute + second;
		
		return billCode;
	}
	
	public void updateStatus(int id, int status) {
		Optional<Order> opt = findById(id);
		Order order = new Order();
		if(opt != null) order = opt.get();
		
		order.setStatus(status);
		save(order);
	}
	
	public List<OrderDto> getAllOrderDto(){
		List<OrderDto> listDto = new ArrayList<OrderDto>();
		List<Order> list = findAll();
		OrderDto dto = new OrderDto();
		
		if(list != null) {
			for(Order order : list) {
				dto = copy(order);
				listDto.add(dto);
			}
		}
		
		return listDto;
	}
	
	public OrderDto copy(Order o) {
		OrderDto dto = new OrderDto();
		
		dto.setId(o.getId());
		dto.setBillCode(o.getBillCode());
		dto.setEmail(o.getEmail());
		dto.setFullname(o.getFullname());
		dto.setPhoneNumber(o.getPhoneNumber());
		dto.setAddress(o.getAddress());
		dto.setNote(o.getNote());
		dto.setOrderTime(o.getOrderTime());
		dto.setStatus(o.getStatus());
		
		if(o.getUser() != null) {
			dto.setUserId(o.getUser().getId());
		}
		
		int amount = 0;
		for(OrderDetail detail : o.getOrderDetails()) {
			amount += detail.getPrice() * detail.getQuantity();
		}
		
		dto.setAmount(amount);
		
		return dto;
	}

	public <S extends Order> S save(S entity) {
		return orderRepository.save(entity);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> findById(Integer id) {
		return orderRepository.findById(id);
	}

	public void delete(Order entity) {
		orderRepository.delete(entity);
	}
	
	
}
