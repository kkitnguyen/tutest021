package com.ttudecor.controller.admin;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
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
	
	@Autowired
	private ModelMapper mapper;
	
	//Show list users
	@RequestMapping("")
	public String show(Model model) {
		
		List<UserDto> listUsers = userService.getAllUserDto();
		
		model.addAttribute("users", listUsers);
		
		model.addAttribute("userManager", true);
		return "admin/list-users";
	}
	
	//Add new user
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
	
		model.addAttribute("user", userDto);
		model.addAttribute("userManager", true);
		
		boolean success = userService.register(model, userDto, password, rePassword);
		
		if(success) {
			model.addAttribute("user", new UserDto());
			model.addAttribute("success", "Thêm mới tài khoản thành công.");
		}
		return "admin/add-user";
		
	}
	
	//Detail user
	@GetMapping("detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		
		Optional<User> opt = userService.findById(id);
		User user = new User();
		if(opt != null) user = opt.get();
		
		UserDto userDto = mapper.map(user, UserDto.class);

		model.addAttribute("user", userDto);
		
		model.addAttribute("userManager", true);
		return "admin/edit-user";
	}
	
	//Update Rule user
	@PostMapping("update")
	public String edit(Model model, @ModelAttribute("user") UserDto userDto) {
		
		if(userService.updateRule(userDto.getId(), userDto.isIsadmin()))
			model.addAttribute("success", "Cập nhật thông tin thành công");
		else model.addAttribute("message", "Có lỗi xảy ra");
		
		model.addAttribute("user", userDto);
		model.addAttribute("userManager", true);
		return "admin/edit-user";
	}
	
}
