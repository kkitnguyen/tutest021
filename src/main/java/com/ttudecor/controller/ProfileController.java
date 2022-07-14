package com.ttudecor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.ttudecor.entity.User;
import com.ttudecor.service.OrderDetailService;
import com.ttudecor.service.OrderService;
import com.ttudecor.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	private UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailService orderDetailService;
	
	@Autowired
	HttpSession session;
	
	@RequestMapping("")
	public String show(Model model) {
			
		int id = (int) session.getAttribute("userId");
		
		Optional<User> opt = userService.findById(id);
		User user = new User();
		if(opt != null) user = opt.get();
		
		UserDto userDto = userService.copy(user);

		model.addAttribute("user", userDto);
		
		return "shop/profile";
	}

	@PostMapping("update")
	public String update(Model model, @ModelAttribute("user") UserDto userDto) {
		int id = (int) session.getAttribute("userId");
		
		Optional<User> opt = userService.findById(id);
		User user = new User();
		if(opt != null) user = opt.get();
		
		user.setFullname(userDto.getFullname());
		user.setEmail(userDto.getEmail());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setAddress(userDto.getAddress());
		
		userService.save(user);
		
		model.addAttribute("user", userDto);
		model.addAttribute("message", "Cập nhật thông tin thành công");
		
		return "shop/profile";
	}
	
	@GetMapping("order")
	public String order(Model model) {

		int id = (int) session.getAttribute("userId");
		
		Optional<User> opt = userService.findById(id);
		User user = new User();
		if(opt != null) user = opt.get();
		
		List<Order> list = user.getOrders();
		
		List<OrderDto> listOrders = new ArrayList<OrderDto>();
		
		OrderDto dto = new OrderDto();
		
		if(list != null) {
			for(Order order : list) {
				dto = orderService.copy(order);
				listOrders.add(dto);
			}
		}
		
		model.addAttribute("orders", listOrders);
	
		return "shop/profile";
		
	}
	
	@GetMapping("order/detail/{id}")
	public String edit(Model model, @PathVariable("id") Integer orderId) {

		Optional<Order> opt = orderService.findById(orderId);
		Order order = new Order();
		if(opt != null) order = opt.get();
		
		if(order.getUser() == null) 
			return "redirect:/profile/order";
		else {
			int userId = (int) session.getAttribute("userId");
			
			if(order.getUser().getId() != userId)
				return "redirect:/profile/order";
		}
		
		OrderDto dto = orderService.copy(order);
		
		model.addAttribute("order", dto);
		model.addAttribute("orderDetails", order.getOrderDetails());

		return "shop/profile";
	}
	
	@GetMapping("change-password")
	public String changePassword(Model model) {
		
		model.addAttribute("changePassword", true);
		
		return "shop/profile";
		
	}
	
	@PostMapping("change-password")
	public String changePassword(Model model, @RequestParam String oldPassword,
			@RequestParam String password, @RequestParam String rePassword) {
		
		int id = (int) session.getAttribute("userId");
		
		Optional<User> opt = userService.findById(id);
		User user = new User();
		if(opt != null) user = opt.get();
		
		if(!user.getPassword().equals(oldPassword)) 
			model.addAttribute("message", "Nhập sai mật khẩu cũ");
		else if(!rePassword.equals(password))
			model.addAttribute("message", "Nhập lại mật khẩu không khớp");
		else if(password.length() < 6)
			model.addAttribute("message", "Mật khẩu tối thiểu 6 ký tự");
		else {
			user.setPassword(password);
			userService.save(user);
			model.addAttribute("message", "Đổi mật khẩu thành công");
		}
		
		model.addAttribute("changePassword", true);
		
		return "shop/profile";
		
	}
	
	
	
	
}
