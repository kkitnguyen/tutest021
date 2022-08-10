package com.ttudecor.service;

import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
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

	private final GalleryService galleryService;
	
	private final StringFormatUtils stringFormatUtils;
	
	private final UploadUtils uploadUtils;
	
	private final ModelMapper mapper;
	
	@Autowired
	public ProductService(ProductRepository productRepository, CategoryService categoryService,
			GalleryService galleryService, StringFormatUtils stringFormatUtils, 
			UploadUtils uploadUtils, ModelMapper mapper) {
		super();
		this.productRepository = productRepository;
		this.galleryService = galleryService;
		this.stringFormatUtils = stringFormatUtils;
		this.uploadUtils = uploadUtils;
		this.mapper = mapper;
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
	
	public Product findProductById(Integer id) {
		try {
			Optional<Product> opt = findById(id);
			return opt.get();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void delete(Product entity) {
		productRepository.delete(entity);
	}
	
	public void deleteById(Integer id) {
		productRepository.deleteById(id);
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
	
	public Page<Product> findByNameContaining(String name, Pageable pageable) {
		return productRepository.findByNameContaining(name, pageable);
	}

	//get Page ProductDto
	public Page<ProductDto> getProductDtoPaginated(Pageable pageable){
		Page<Product> page = findAll(pageable);
		
		Page<ProductDto> pageDto = page.map(p -> {
			ProductDto dto = new ProductDto();
			dto = copy(p);
			return dto;
		});
		
		return pageDto;

	}
	
	///get Page ProductDto by categoryId
	public Page<ProductDto> getProductDtoByCategoryIdPaginated(int categoryId, Pageable pageable){
		Page<Product> page = findByCategoryId(categoryId, pageable);
		
		Page<ProductDto> pageDto = page.map(p -> {
			ProductDto dto = new ProductDto();
			dto = copy(p);
			return dto;
		});
		
		return pageDto;

	}
	
	//get Page ProductDto find by name
	public Page<ProductDto> getProductDtoByNamePaginated(String name, Pageable pageable){
		Page<Product> page = findByNameContaining(name, pageable);
		
		Page<ProductDto> pageDto = page.map(p -> {
			ProductDto dto = new ProductDto();
			dto = copy(p);
			return dto;
		});
		
		return pageDto;

	}
	
	//Find a number of ProductDto by categoryId
	public List<ProductDto> getProductDtoByCategoryIdLimitTo(int categoryId, int limit){
		Pageable pageable = PageRequest.of(0, limit);
		
		Page<Product> page = findByCategoryId(categoryId, pageable);
		
		List<ProductDto> products = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			products.add(copy(product));
		}
		
		return products;
	}
	
	//Find a number of ProductDto
	public List<ProductDto> getProductDtoLimitTo(int limit){
		Pageable pageable = PageRequest.of(0, limit);
		
		Page<Product> page = findAll(pageable);
		
		List<ProductDto> products = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			products.add(copy(product));
		}
		
		return products;
	}
	
	//Find a number of random ProductDto 
	public List<ProductDto> getRandomProductLimitTo(int limit){
		List<ProductDto> list = getAllProductDto();
		if(limit >= list.size()) return list;
		
		List<ProductDto> randomList = new ArrayList<ProductDto>();
		
		//Choose a product from list of all products and remove it from list
		Random rand = new Random();
		for(int i = 0 ; i< limit ; i++) {
			int num = rand.nextInt(list.size() - 1);
			
			randomList.add(list.get(num));
			list.remove(num);
		}
		
		return randomList;
	}
	
	//Find a number of ProductDto sort by product.createdTime
	public List<ProductDto> findNewestProductLimitTo(int limit){
		Pageable pageable = PageRequest.of(0, limit, Sort.by(Direction.DESC, "createdTime"));
		
		Page<Product> page = findAll(pageable);
		
		List<ProductDto> newestProducts = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			newestProducts.add(copy(product));
		}
		
		return newestProducts;
	}
	
	//Find a number of ProductDto sort by product.sold
	public List<ProductDto> findBestSellerProductLimitTo(int limit){
		Pageable pageable = PageRequest.of(0, limit, Sort.by(Direction.DESC, "sold"));
		
		Page<Product> page = findAll(pageable);
		
		List<ProductDto> products = new ArrayList<ProductDto>();
		for(Product product : page.toList()) {
			products.add(copy(product));
		}
		
		return products;
	}

	//Find all Products and map to ProductDto
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
	
	//Search Product by name
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

	//Copy properties from ProductDto to Product
	public Product copy(ProductDto dto) {
		Product p = new Product();
		
		if(dto.getId() != null) {
			Optional<Product> opt = findById(dto.getId());
			if(opt != null) p = opt.get();
		}
		
		Category category = new Category();
		category.setId(dto.getCategoryId());
		
		p.setName(dto.getName());
		p.setCategory(category);
		p.setPrice(dto.getPrice());
		p.setDiscountPrice(dto.getDiscountPrice());
		p.setQuantity(dto.getQuantity());
		p.setDescription(dto.getDescription());
		p.setUrl(dto.getUrl());
		
		return p;
	}
	
	//Copy properties from Product to ProductDto 
	public ProductDto copy(Product p) {
		ProductDto dto = mapper.map(p, ProductDto.class);

		dto.setCategoryId(p.getCategory().getId());

		LocalDateTime now = LocalDateTime.now();
		
		//set product is new if product was created in last 15 days
		if(p.getCreatedTime().isBefore(now.minusDays(15)))
			dto.setIsnew(false);
			
		else dto.setIsnew(true);
		
		return dto;
	}
	
	public String createProductUrl(String productName, int productId) {
		String nameFormat = stringFormatUtils.convertToUrlFomart(productName);
		String idFormat = "p" + String.format("%03d", productId);
		
		return nameFormat + "-" + idFormat;
	}
	
	
	//Add new product, save image and gallery of product to server
	public void addProduct(ProductDto dto, MultipartFile image,
			MultipartFile[] gallery, String uploadPath) {
		
		Product product = copy(dto);

		product.setCreatedTime(LocalDateTime.now());
		product.setSold(0);
		product.setUrl("newurl");
		save(product); //save product first time to get id
		
		product = findByUrlEquals("newurl");
		
		//Create url of product. Ex: product-name-p001
		String nameFormat = stringFormatUtils.convertToUrlFomart(product.getName());
		String idFormat = "p" + String.format("%03d", product.getId());
		String url = nameFormat + "-" + idFormat;
		
		product.setUrl(url);
		
		//----- Upload image
		//Save image if image is uploaded: images/products/p001/p001.jpg
		if(image.getSize() > 0) {
			String imageSavedName = uploadUtils.uploadImage(image, uploadPath + File.separator + idFormat, idFormat);
			
			System.out.println("File separator ---------------------------: " + File.separator);
			product.setImage("/images/products/" + idFormat + "/" + imageSavedName);
		}
		
		save(product);
		
		
		//--------- Upload gallery
		//true if gallery is uploaded
		boolean hasGallery = false;
		for(MultipartFile file : gallery) 
			if(file.getSize() > 0) {
				hasGallery = true;
				break;
			}

		//Save gallery if gallery is uploaded
		if(hasGallery) {
			
			//   images/products/p001/gallery
			uploadPath = uploadPath + File.separator + idFormat + File.separator + "gallery";
			
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
	
	//Update product, save image and gallery of product to server
	public void updateProduct(ProductDto dto, MultipartFile image,
			MultipartFile[] gallery, String uploadPath) {

		Product product = copy(dto);
		product.setUpdatedTime(LocalDateTime.now());

		String nameFormat = stringFormatUtils.convertToUrlFomart(product.getName());
		String idFormat = "p" + String.format("%03d", product.getId());
		String url = nameFormat + "-" + idFormat;
		
		product.setUrl(url);
		
		if(image.getSize() > 0) {
			String imageSavedName = uploadUtils.uploadImage(image, uploadPath + File.separator + idFormat, idFormat);
			product.setImage("/images/products/" + idFormat + "/" + imageSavedName);
		}
		
		save(product);
		
		// Upload gallery
		boolean hasGallery = false;
		for(MultipartFile file : gallery) 
			if(file.getSize() > 0) {
				hasGallery = true;
				break;
			}
		
		if(hasGallery) {
			uploadPath = uploadPath + File.separator + idFormat + File.separator + "gallery";
			
			//Delete old gallery
			uploadUtils.deleteFolder(uploadPath);
			
			List<Gallery> list = product.getGallery();
			if(list != null) {
				for(int i = 0; i < list.size() ; i++) {
					Gallery g = list.get(i);
					list.set(i, null);
					galleryService.delete(g);
				}
			}
			
			//Save new gallery
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

	//Get list Product from Cookie and save Product to Cookie
	public List<ProductDto> getRelatedProducts(HttpServletResponse response, String json, Product product) {
		
		//---- Get list Product from Cookie
		json = EncryptionUtils.dencrypt("KEYKEYKEY", json);
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<Integer>>(){}.getType();
		List<Integer> listId = gson.fromJson(json, type);
		
		List<ProductDto> listProduct = new ArrayList<ProductDto>();

		if(listId != null) {	
			for(int id : listId) {
				Product p = findProductById(id);
				listProduct.add(copy(p));
			}
		}else listId = new ArrayList<>();
		
		
		//---- Save Product to Cookie
		
		// max size is 4
		if(listId.size() >= 4 && !listId.contains(product.getId())) 
			listId.remove(0);
		
		//add product to list
		if(!listId.contains(product.getId())) 
			listId.add(product.getId());
		
		json = gson.toJson(listId);
		
		json = EncryptionUtils.encrypt("KEYKEYKEY", json);
		
		Cookie cookie = new Cookie("related_product", json);
		cookie.setMaxAge(7 * 24 * 60  * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		return listProduct;
	}

	
	
}
