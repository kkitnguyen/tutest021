package com.ttudecor.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttudecor.dto.ProductDto;
import com.ttudecor.entity.Category;
import com.ttudecor.entity.Gallery;
import com.ttudecor.entity.Product;
import com.ttudecor.repository.ProductRepository;
import com.ttudecor.utils.EncryptionUtils;
import com.ttudecor.utils.StringFormatUtils;
import com.ttudecor.utils.UploadUtils;

@Service
public class ProductService {
	private final ProductRepository productRepository;
	
	private final CategoryService categoryService;
	
	private final GalleryService galleryService;
	
	private final StringFormatUtils stringFormatUtils;
	
	private final UploadUtils uploadUtils;
	
	@Autowired
	public ProductService(ProductRepository productRepository, CategoryService categoryService,
			GalleryService galleryService, StringFormatUtils stringFormatUtils, UploadUtils uploadUtils) {
		super();
		this.productRepository = productRepository;
		this.categoryService = categoryService;
		this.galleryService = galleryService;
		this.stringFormatUtils = stringFormatUtils;
		this.uploadUtils = uploadUtils;
	}

	public <S extends Product> S save(S entity) {
		return productRepository.save(entity);
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Optional<Product> findById(Integer id) {
		return productRepository.findById(id);
	}

	public void delete(Product entity) {
		productRepository.delete(entity);
	}
	
	public Product findByUrlEquals(String url) {
		return productRepository.findByUrlEquals(url);
	}
	
	public List<Product> findAll(Sort sort) {
		return productRepository.findAll(sort);
	}
	
	public Integer countByCategoryId(int categoryId) {
		return productRepository.countByCategoryId(categoryId);
	}

	public Page<Product> findByCategoryId(int categoryId, Pageable pageable) {
		return productRepository.findByCategoryId(categoryId, pageable);
	}

	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	public List<Product> findByNameContaining(String name) {
		return productRepository.findByNameContaining(name);
	}

	
	public List<ProductDto> getProductDtoByCategoryIdLimitTo(int categoryId, int limit){
		Pageable pageable = PageRequest.of(0, limit);
		
		Page<Product> page = findByCategoryId(categoryId, pageable);
		
		List<ProductDto> products = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			products.add(copy(product));
		}
		
		return products;
	}
	
	public List<ProductDto> getProductDtoLimitTo(int limit){
		Pageable pageable = PageRequest.of(0, limit);
		
		Page<Product> page = findAll(pageable);
		
		List<ProductDto> products = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			products.add(copy(product));
		}
		
		return products;
	}
	
	public List<ProductDto> getRandomProductLimitTo(int limit){
		List<ProductDto> list = getAllProductDto();
		if(limit >= list.size()) return list;
		
		List<ProductDto> randomList = new ArrayList<ProductDto>();
		
		Random rand = new Random();
		for(int i = 0 ; i< limit ; i++) {
			int num = rand.nextInt(list.size() - 1);
			
			randomList.add(list.get(num));
			list.remove(num);
		}
		
		return randomList;
	}
	
	public List<ProductDto> findNewestProductLimitTo(int limit){
		Pageable pageable = PageRequest.of(0, limit, Sort.by(Direction.DESC, "createdDate"));
		
		Page<Product> page = findAll(pageable);
		
		List<ProductDto> newestProducts = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			newestProducts.add(copy(product));
		}
		
		return newestProducts;
	}
	
	public List<ProductDto> findBestSellerProductLimitTo(int limit){
		Pageable pageable = PageRequest.of(0, limit, Sort.by(Direction.DESC, "sold"));
		
		Page<Product> page = findAll(pageable);
		
		List<ProductDto> products = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			products.add(copy(product));
		}
		
		return products;
	}

	public List<ProductDto> getAllProductDto(){
		List<Product> list = findAll();
		
		List<ProductDto> listDto = new ArrayList<ProductDto>();
		
		if(list != null) {
			for(Product p : list) {
				ProductDto dto = copy(p);
				listDto.add(dto);
			}
		}
		
		return listDto;
	}
	
	public List<ProductDto> getProductDtoByName(String name){
		List<Product> list = findByNameContaining(name);
		
		List<ProductDto> listDto = new ArrayList<ProductDto>();
		
		if(list != null) {
			for(Product p : list) {
				ProductDto dto = copy(p);
				listDto.add(dto);
			}
		}
		
		return listDto;
	}

	public Product copy(ProductDto dto) {
		Product p = new Product();
		if(dto.getId() != null) {
			Optional<Product> opt = findById(dto.getId());
			if(opt != null) p = opt.get();
		}
		
		p.setName(dto.getName());
		p.setDescription(dto.getDescription());
		p.setPrice(dto.getPrice());
		p.setDiscountPrice(dto.getDiscountPrice());
		p.setQuantity(dto.getQuantity());
		p.setUrl(dto.getUrl());
		
		return p;
	}
	
	public ProductDto copy(Product p) {
		ProductDto dto = new ProductDto();
		
		dto.setId(p.getId());
		dto.setName(p.getName());
		dto.setImage(p.getImage());
		dto.setCreatedDate(p.getCreatedDate());
		dto.setDescription(p.getDescription());
		dto.setPrice(p.getPrice());
		dto.setDiscountPrice(p.getDiscountPrice());
		dto.setQuantity(p.getQuantity());
		dto.setSold(p.getSold());
		dto.setCategoryId(p.getCategory().getId());
		dto.setUrl(p.getUrl());
		
		Date now = new Date();
		Date sevenDaysBefore = new Date(now.getTime() - (1000*24*60*60*7));
		
		if(p.getCreatedDate().compareTo(sevenDaysBefore) < 0)
			dto.setIsnew(false);
		else dto.setIsnew(true);
		
		return dto;
	}
	
	public void addProduct(ProductDto dto, MultipartFile image,
			MultipartFile[] gallery, String uploadPath) {
		
		Optional<Category> opt = categoryService.findById(dto.getCategoryId());
		
		Product product = copy(dto);
		product.setCategory(opt.get());

		Date now = new Date();
		product.setCreatedDate(now);
		product.setUrl("newurl");
		product.setSold(0);
		
		save(product);
		
		product = findByUrlEquals("newurl");
		
		String nameFormat = stringFormatUtils.convertToUrlFomart(product.getName());
		String idFormat = "p" + String.format("%03d", product.getId());
		String url = nameFormat + "-" + idFormat;
		
		product.setUrl(url);
		
		String imageSavedName = uploadUtils.uploadImage(image, uploadPath + "\\" + idFormat, idFormat);
		product.setImage("/images/products/" + idFormat + "/" + imageSavedName);
		
		save(product);

		if(gallery.length > 0) {
			uploadPath = uploadPath + "\\" + idFormat + "\\" + "gallery";
			
			int i = 0;
			for(MultipartFile file : gallery) {
				if(file.getSize() > 0) {
					i++;
					imageSavedName = uploadUtils.uploadImage(file, uploadPath, idFormat + "-" + i);
					Gallery g = new Gallery();
					g.setProduct(product);
					g.setImage("/images/products/" + idFormat + "/gallery/" + imageSavedName);
					galleryService.save(g);
				}
		
			}
		}
		
	}
	
	public void updateProduct(ProductDto dto, MultipartFile image,
			MultipartFile[] gallery, String uploadPath) {
		
		Optional<Category> opt = categoryService.findById(dto.getCategoryId());
		
		Product product = copy(dto);
		product.setCategory(opt.get());

		String nameFormat = stringFormatUtils.convertToUrlFomart(product.getName());
		String idFormat = "p" + String.format("%03d", product.getId());
		String url = nameFormat + "-" + idFormat;
		
		product.setUrl(url);
		
		if(image.getSize() > 0) {
			String imageSavedName = uploadUtils.uploadImage(image, uploadPath + "\\" + idFormat, idFormat);
			product.setImage("/images/products/" + idFormat + "/" + imageSavedName);
		}
		
		save(product);

		if(gallery.length > 0) {
			uploadPath = uploadPath + "\\" + idFormat + "\\" + "gallery";
			
			uploadUtils.deleteFolder(uploadPath);
			
			List<Gallery> list = product.getGallery();
			if(list != null) {
				for(int i = 0; i < list.size() ; i++) {
					Gallery g = list.get(i);
					list.set(i, null);
					galleryService.delete(g);
				}
			}
				
			int i = 0;
			for(MultipartFile file : gallery) {
				if(file.getSize() > 0) {
					i++;
					String imageSavedName = uploadUtils.uploadImage(file, uploadPath, idFormat + "-" + i);
					Gallery g = new Gallery();
					g.setProduct(product);
					g.setImage("/images/products/" + idFormat + "/gallery/" + imageSavedName);
					galleryService.save(g);
				}
				
			}
		}		
	}

	public List<ProductDto> getRelatedProducts(HttpServletResponse response, String json, Product product) {
		json = EncryptionUtils.dencrypt("KEYKEYKEY", json);
		
		Gson gson = new Gson();
		
		Type type = new TypeToken<List<String>>(){}.getType();
		List<String> listUrl = gson.fromJson(json, type);
		
		List<ProductDto> list = new ArrayList<ProductDto>();
		
		if(listUrl != null) {
			if(listUrl.size() >= 4 && !listUrl.contains(product.getUrl())) listUrl.remove(0);
			
			for(String url : listUrl) {
				url = url.substring(url.length() - 3);
				
				int id = Integer.parseInt(url);
				
				Optional<Product> opt = findById(id);
				Product p = new Product();
				if(opt != null) p = opt.get();
				
				list.add(copy(p));
			}
		}else listUrl = new ArrayList<>();
		
		if(!listUrl.contains(product.getUrl())) {
			listUrl.add(product.getUrl());
			list.add(copy(product));
		}
		
		json = gson.toJson(listUrl);
		
		json = EncryptionUtils.encrypt("KEYKEYKEY", json);
		
		Cookie cookie = new Cookie("related_product", json);
		cookie.setMaxAge(7 * 24 * 60  * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		return list;
	}

	
	
}
