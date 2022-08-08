package com.ttudecor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ttudecor.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query("select o from Order o where o.user.id = ?1")
	List<Order> findByUserId(int userId);
}
