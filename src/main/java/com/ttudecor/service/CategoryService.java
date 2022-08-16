package com.ttudecor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ttudecor.dto.CategoryDto;
import com.ttudecor.entity.Category;
import com.ttudecor.repository.CategoryRepository;
import com.ttudecor.utils.StringFormatUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	private final StringFormatUtils stringFormatUtils;

	public <S extends Category> S save(S entity) {
		return categoryRepository.save(entity);
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Optional<Category> findById(Integer id) {
		return categoryRepository.findById(id);
	}
	
	public void deleteById(Integer id) {
		categoryRepository.deleteById(id);
	}
	
	public Category findCategoryById(Integer id) {
		try {
			Optional<Category> opt = findById(id);
			
			return opt.get();
		} catch (Exception e) {
			return null;
		}
		
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
		
		if(c.getProducts() != null)
			dto.setNumberOfProduct(c.getProducts().size());
		else dto.setNumberOfProduct(0);
		return dto;
	}
	
	//Add new category or update category name
	public boolean AddOrUpdateCategory(Category category) {
		try {
			String nameFormat = stringFormatUtils.convertToUrlFomart(category.getName());
			category.setUrl("");
			category = save(category); //save first time to auto generate id
			
			String idFormat = "c" + String.format("%02d", category.getId());
			String url = nameFormat + "-" + idFormat;
			
			category.setUrl(url);
			save(category);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	
}
