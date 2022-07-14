package com.ttudecor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttudecor.entity.Feedback;
import com.ttudecor.repository.FeedbackRepository;

@Service
public class FeedbackService {
	private final FeedbackRepository feedbackRepository;
	
	@Autowired
	public FeedbackService(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}

	public <S extends Feedback> S save(S entity) {
		return feedbackRepository.save(entity);
	}

	public List<Feedback> findAll() {
		return feedbackRepository.findAll();
	}

	public Optional<Feedback> findById(Integer id) {
		return feedbackRepository.findById(id);
	}

	public void delete(Feedback entity) {
		feedbackRepository.delete(entity);
	}
	
	
}
