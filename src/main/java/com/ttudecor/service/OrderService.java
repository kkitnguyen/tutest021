package com.ttudecor.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
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
	private final OrderRepository orderRepository;
	private final ProductService productService;
	private final OrderDetailService orderDetailService;
	private final ModelMapper mapper;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public OrderService(OrderRepository orderRepository, ProductService productService,
			OrderDetailService orderDetailService,  ModelMapper mapper) {
		this.orderRepository = orderRepository;
		this.productService = productService;
		this.orderDetailService = orderDetailService;
		this.mapper = mapper;
	}

	
	public void checkout(Collection<CartItemDto> listCart, OrderDto orderDto) {
		
		LocalDateTime now = LocalDateTime.now();
		String billCode = createBillCode(now);
		
		Order order = mapper.map(orderDto, Order.class);
		order.setBillCode(billCode);
		order.setOrderTime(now);
		order.setStatus(0);
		
		//Set user for order if customer logged in
		if(session.getAttribute("fullname") != null) {
			User user = new User();
			user.setId((Integer) session.getAttribute("userId"));
			order.setUser(user);
		}
		save(order);
		
		for(CartItemDto item : listCart) {
			//increase product sold and reduce product quantity
			Product product = productService.findProductById(item.getProductId());
			product.setQuantity(product.getQuantity() - item.getQuantity());
			product.setSold(product.getSold() + item.getQuantity());
			productService.save(product);
			
			//Save order detail
			OrderDetail detail = new OrderDetail(order, product, item.getPrice(), item.getQuantity());
			orderDetailService.save(detail);
		}
	}
	
	// Create billcode by datetime: TUyyMMddHHmmss
	public String createBillCode(LocalDateTime now) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		
		String billCode = "TU" + now.format(formatter);
		return billCode;
	}
	
	public void updateStatus(int id, int status) {
		Order order = findOrderById(id);
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
		OrderDto dto = mapper.map(o, OrderDto.class);
		
		//set user id
		if(o.getUser() != null) {
			dto.setUserId(o.getUser().getId());
		}
		
		//set total amount
		int amount = 0;
		for(OrderDetail detail : o.getOrderDetails()) {
			amount += detail.getPrice() * detail.getQuantity();
		}
		dto.setAmount(amount);
		
		//format time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
		dto.setOrderTime(o.getOrderTime().format(formatter));

		return dto;
	}
	
	public List<OrderDto> findOrderDtoByUserId(int userId) {
		List<Order> list = findByUserId(userId);
		
		List<OrderDto> listDto = new ArrayList<>();
		OrderDto dto = new OrderDto();
		
		if(list != null)
			for(Order order : list) {
				dto = copy(order);
				listDto.add(dto);
			}
		
		return listDto;
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
	
	public Order findOrderById(Integer id) {
		try {
			Optional<Order> opt = findById(id);
			return opt.get();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void delete(Order entity) {
		orderRepository.delete(entity);
	}

	public List<Order> findByUserId(int userId) {
		return orderRepository.findByUserId(userId);
	}
	
	
	
}
