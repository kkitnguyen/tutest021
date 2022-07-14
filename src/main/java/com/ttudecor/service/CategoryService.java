package com.ttudecor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttudecor.dto.CategoryDto;
import com.ttudecor.entity.Category;
import com.ttudecor.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public <S extends Category> S save(S entity) {
		return categoryRepository.save(entity);
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Optional<Category> findById(Integer id) {
		return categoryRepository.findById(id);
	}

	public void delete(Category entity) {
		categoryRepository.delete(entity);
	}
	
	
	public List<CategoryDto> getAllCategoryDto(){
		List<Category> list = findAll();
		
		List<CategoryDto> listDto = new ArrayList<CategoryDto>();
		
		if(list != null) {
			for (Category c : list) {
				CategoryDto dto = copy(c);
				listDto.add(dto);
			}
		}
		
		return listDto;
	}
	
	public CategoryDto copy(Category c) {
		CategoryDto dto = new CategoryDto();
		dto.setId(c.getId());
		dto.setName(c.getName());
		dto.setUrl(c.getUrl());
		dto.setNumberOfProduct(c.getProducts().size());
		
		return dto;
	}
}
