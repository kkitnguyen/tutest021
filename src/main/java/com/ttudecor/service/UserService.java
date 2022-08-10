package com.ttudecor.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ttudecor.dto.UserDto;
import com.ttudecor.entity.User;
import com.ttudecor.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	private final ModelMapper mapper;
	
	@Autowired
	public UserService(UserRepository userRepository, ModelMapper mapper) {
		this.userRepository = userRepository;
		this.mapper = mapper;
	}

	public <S extends User> S save(S entity) {
		return userRepository.save(entity);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findById(Integer id) {
		return userRepository.findById(id);
	}
	
	public User findUserById(Integer id) {
		try {
			Optional<User> opt = findById(id);
			return opt.get();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void delete(User entity) {
		userRepository.delete(entity);
	}
	
	public User findByEmailAndPassword(String email, String password) {
		User user = findByEmail(email);
		
		if(user != null) {
			boolean correct = BCrypt.checkpw(password, user.getPassword());
			
			if(correct) return user;
		}
		
		return null;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public UserDto findUserDtoById(Integer id) {
		try {
			Optional<User> opt = findById(id);
			User user = new User();
			if(opt != null) user = opt.get();
			
			UserDto userDto = mapper.map(user, UserDto.class);
			
			return userDto;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	//find all User and map to list UserDto
	public List<UserDto> getAllUserDto(){
		
		List<User> list = findAll();
		
		UserDto dto = new UserDto();
		List<UserDto> listDto = new ArrayList<UserDto>();
		
		if(list != null)
			for(User user : list) {
				dto = mapper.map(user, UserDto.class);
				listDto.add(dto);
			}
		
		return listDto;
	}
	
	public boolean register(Model model, UserDto userDto, String password, String rePassword) {
		User user = new User();
		user = findByEmail(userDto.getEmail());

		//vallidate
		if(user != null) 
			model.addAttribute("message", "Email đã tồn tại, vui lòng chọn email khác.");
		else if(!password.equals(rePassword)) 
			model.addAttribute("message", "Nhập lại mật khẩu không khớp.");
		else if(password.length() < 6) 
			model.addAttribute("message", "Mật khẩu tối thiểu 6 ký tự.");
		else {
			//add new
			user = new User();
			user = mapper.map(userDto, User.class);
			
			user.setCreatedTime(LocalDateTime.now());
			
			String hassPassword = BCrypt.hashpw(password, BCrypt.gensalt());
			user.setPassword(hassPassword);
			save(user);
			
			return true;
		}
		return false;
	}
	
	//Update user profile
	public boolean update(UserDto userDto, int userId) {
		try {
			User user = findById(userId).get();
			
			user.setFullname(userDto.getFullname());
			user.setEmail(userDto.getEmail());
			user.setPhoneNumber(userDto.getPhoneNumber());
			user.setAddress(userDto.getAddress());
			
			save(user);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	//Update rule
	public boolean updateRule(int userId, boolean isadmin) {
		try {
			User user = findById(userId).get();
			user.setIsadmin(isadmin);
			save(user);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean changePassword(Model model, int userId, String oldPassword, 
			String password, String rePassword) {
		try {
			User user = findUserById(userId);
			
			boolean correct = BCrypt.checkpw(oldPassword, user.getPassword());
			
			if(!correct) 
				model.addAttribute("message", "Nhập sai mật khẩu cũ");
			else if(!rePassword.equals(password))
				model.addAttribute("message", "Nhập lại mật khẩu không khớp");
			else if(password.length() < 6)
				model.addAttribute("message", "Mật khẩu tối thiểu 6 ký tự");
			else {
				String hassPassword = BCrypt.hashpw(password, BCrypt.gensalt());
				user.setPassword(hassPassword);
				
				save(user);
				model.addAttribute("message", "Đổi mật khẩu thành công");
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
		
	}
}
