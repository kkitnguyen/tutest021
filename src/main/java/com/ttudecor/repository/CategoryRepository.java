package com.ttudecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttudecor.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
}
