package com.ttudecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ttudecor.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer>{

}
