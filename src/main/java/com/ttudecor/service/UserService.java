package com.ttudecor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttudecor.dto.UserDto;
import com.ttudecor.entity.User;
import com.ttudecor.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
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

	public void delete(User entity) {
		userRepository.delete(entity);
	}
	
	public User findByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	
	
	public User copy(UserDto dto) {
		User user = new User();
		if(dto.getId() != null) {
			Optional<User> opt = findById(dto.getId());
			if(opt != null) user = opt.get();
		}
		
		user.setId(dto.getId());
		user.setFullname(dto.getFullname());
		user.setEmail(dto.getEmail());
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setAddress(dto.getAddress());
		user.setIsadmin(dto.isIsadmin());
		
		return user;
	}
	
	public UserDto copy(User user) {
		UserDto dto = new UserDto();
		
		dto.setId(user.getId());
		dto.setFullname(user.getFullname());
		dto.setEmail(user.getEmail());
		dto.setPhoneNumber(user.getPhoneNumber());
		dto.setAddress(user.getAddress());
		dto.setCreatedDate(user.getCreatedDate());
		dto.setLastUpdatedDate(user.getLastUpdatedDate());
		dto.setIsadmin(user.isIsadmin());
		
		return dto;
		
	}
	
	
	public List<UserDto> getAllUserDto(){
		List<UserDto> listDto = new ArrayList<UserDto>();
		List<User> list = new ArrayList<User>();
		UserDto dto = new UserDto();
		
		if(list != null) {
			for(User user : findAll()) {
				dto = copy(user);
				listDto.add(dto);
			}
			
		}
		
		return listDto;
	}


}
