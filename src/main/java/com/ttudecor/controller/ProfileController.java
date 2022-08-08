package com.ttudecor.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.dto.OrderDto;
import com.ttudecor.dto.UserDto;
import com.ttudecor.entity.Order;
import com.ttudecor.service.OrderService;
import com.ttudecor.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private HttpSession session;
	
	//show information
	@RequestMapping("")
	public String show(Model model) {
		int id = (int) session.getAttribute("userId");

		model.addAttribute("user", userService.findUserDtoById(id));
		
		return "shop/profile";
	}

	//update user
	@PostMapping("update")
	public String update(Model model, @ModelAttribute("user") UserDto userDto) {
		int id = (int) session.getAttribute("userId");
		
		model.addAttribute("user", userDto);
		
		if(userService.update(userDto, id)) 
			model.addAttribute("message", "Cập nhật thông tin thành công");
		else
			model.addAttribute("message", "Có lỗi xảy ra. Cập nhật thất bại.");
		
		return "shop/profile";
	}
	
	//Show list order
	@GetMapping("order")
	public String listOrder(Model model) {

		int id = (int) session.getAttribute("userId");

		List<OrderDto> listOrders = orderService.findOrderDtoByUserId(id);
		model.addAttribute("orders", listOrders);
	
		return "shop/profile";
		
	}
	
	//order detail
	@GetMapping("order/detail/{id}")
	public String orderDetail(Model model, @PathVariable("id") Integer orderId) {
		
		try {
			Order order = orderService.findById(orderId).get();
			
			//order of guest
			if(order.getUser() == null) 
				return "redirect:/profile/order"; 
			else {
				int userId = (int) session.getAttribute("userId");
				
				//order of other user
				if(order.getUser().getId() != userId)
					return "redirect:/profile/order";
			}
			
			OrderDto dto = orderService.copy(order);
			
			model.addAttribute("order", dto);
			model.addAttribute("orderDetails", order.getOrderDetails());

			return "shop/profile";
		} catch (Exception e) {
			return "redirect:/profile/order";
		}
		
	}
	
	
	//Change password
	@GetMapping("change-password")
	public String changePassword(Model model) {
		
		model.addAttribute("changePassword", true);
		
		return "shop/profile";
		
	}
	
	@PostMapping("change-password")
	public String changePassword(Model model, @RequestParam String oldPassword,
			@RequestParam String password, @RequestParam String rePassword) {
		
		int id = (int) session.getAttribute("userId");
		userService.changePassword(model, id, oldPassword, password, rePassword);
		
		model.addAttribute("changePassword", true);
		return "shop/profile";
		
	}
	
	
	
	
}
