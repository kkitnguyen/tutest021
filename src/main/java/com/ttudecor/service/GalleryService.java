package com.ttudecor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttudecor.entity.Gallery;
import com.ttudecor.repository.GalleryRepository;

@Service
public class GalleryService {
	private final GalleryRepository galleryRepository;
	
	@Autowired
	public GalleryService(GalleryRepository galleryRepository) {
		super();
		this.galleryRepository = galleryRepository;
	}

	public <S extends Gallery> S save(S entity) {
		return galleryRepository.save(entity);
	}

	public List<Gallery> findAll() {
		return galleryRepository.findAll();
	}

	public void deleteById(Integer id) {
		galleryRepository.deleteById(id);
	}

	public void delete(Gallery entity) {
		galleryRepository.delete(entity);
	}

	public Optional<Gallery> findById(Integer id) {
		return galleryRepository.findById(id);
	}
	
	
	
	
}
