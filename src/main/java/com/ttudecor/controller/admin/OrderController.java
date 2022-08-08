package com.ttudecor.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.dto.OrderDto;
import com.ttudecor.entity.Order;
import com.ttudecor.service.OrderDetailService;
import com.ttudecor.service.OrderService;

@Controller
@RequestMapping("/ttu-admin/order-manager")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailService orderDetailService;
	
	
	@RequestMapping("")
	public String show(Model model) {
		
		List<OrderDto> listOrders = orderService.getAllOrderDto();
		
		model.addAttribute("orders", listOrders);
		model.addAttribute("orderManager", true);
		return "admin/list-orders";
	}
	
	@PostMapping("update")
	public String update(Model model, @RequestParam("id") Integer id,
			@RequestParam("status") Integer status) {
		
		orderService.updateStatus(id, status);
		
		return "redirect:/ttu-admin/order-manager";
	}
	
	@GetMapping("detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		
		Order order = orderService.findOrderById(id);
		OrderDto dto = orderService.copy(order);
		
		model.addAttribute("order", dto);
		model.addAttribute("orderDetails", order.getOrderDetails());
		
		model.addAttribute("orderManager", true);
		return "admin/order-detail";
	}
	
	@PostMapping("/detail/update")
	public String update2(Model model, @RequestParam("id") Integer id, 
			@RequestParam("status") Integer status) {
		orderService.updateStatus(id, status);
		
		Order order = orderService.findOrderById(id);
		OrderDto dto = orderService.copy(order);
		
		model.addAttribute("order", dto);
		model.addAttribute("orderDetails", order.getOrderDetails());
		
		model.addAttribute("orderManager", true);
		return "admin/order-detail";
	}
	
}
