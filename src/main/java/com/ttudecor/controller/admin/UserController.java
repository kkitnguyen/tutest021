package com.ttudecor.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.dto.UserDto;
import com.ttudecor.entity.User;
import com.ttudecor.service.UserService;

@Controller
@RequestMapping("/ttu-admin/account-manager")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("")
	public String show(Model model) {
		
		List<UserDto> listUsers = userService.getAllUserDto();
		
		model.addAttribute("users", listUsers);
		
		model.addAttribute("userManager", true);
		return "admin/list-users";
	}
	
	@GetMapping("add")
	public String add(Model model) {
		UserDto userDto = new UserDto();

		model.addAttribute("user", userDto);
		
		model.addAttribute("userManager", true);
		return "admin/add-user";
	}
	
	@PostMapping("add")
	public String add(Model model, @ModelAttribute("user") UserDto userDto,
			@RequestParam("password") String password,
			 @RequestParam("rePassword") String rePassword) {
		
		User user = new User();
		user = userService.findByEmail(userDto.getEmail());
		
		if(user != null) {
			model.addAttribute("message", "Email đã tồn tại, vui lòng chọn email khác.");
			model.addAttribute("user", userDto);
			model.addAttribute("userManager", true);
			return "admin/add-user";
		}
		else if(!password.equals(rePassword)) {
			model.addAttribute("message", "Nhập lại mật khẩu không khớp.");
			model.addAttribute("user", userDto);
			model.addAttribute("userManager", true);
			return "admin/add-user";
		}
			
		else if(password.length() < 6) {
			model.addAttribute("message", "Mật khẩu tối thiểu 6 ký tự.");
			model.addAttribute("user", userDto);
			model.addAttribute("userManager", true);
			return "admin/add-user";
		}
		else {
			user = new User();
			user = userService.copy(userDto);
			
			Date now = new Date();
			user.setCreatedDate(now);
			user.setPassword(password);
			user.setIsadmin(false);
			userService.save(user);;;
			
			return "redirect:";
		}
		
	}
	
	@GetMapping("detail/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		
		Optional<User> opt = userService.findById(id);
		User user = new User();
		if(opt != null) user = opt.get();
		
		UserDto userDto = userService.copy(user);

		model.addAttribute("user", userDto);
		
		model.addAttribute("userManager", true);
		return "admin/edit-user";
	}
	
}
