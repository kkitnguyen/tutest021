package com.ttudecor.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ttudecor.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	Product findByUrlEquals(String url);
	
	@Query("select p from Product p where p.category.id = ?1")
	Page<Product> findByCategoryId(int categoryId, Pageable pageable);
	
	@Query("select count(p) from Product p where p.category.id = ?1")
	Integer countByCategoryId(int categoryId);
	
	List<Product> findByNameContaining(String name);
	
	Page<Product> findByNameContaining(String name, Pageable pageable);
}
