package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	@Autowired
	private ProductRepository repository;
	@Autowired
	private CategoryRepository categoryRepsository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable){
		Page<Product> list = repository.findAll(pageable);
		
		Page<ProductDTO> listDto = list.map(x -> new ProductDTO(x));
		
		return listDto;
		
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		try {
		Optional<Product> obj = repository.findById(id);
		Product entity =  obj.orElseThrow(() -> new EntityNotFoundException("Entinty not found"));
		
		return new ProductDTO(entity, entity.getCategories());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found "+ id);
		}
	}

	@Transactional
	public ProductDTO inset(ProductDTO dto) {
		Product entity = new Product();
		copyDTOToEntity(dto, entity);
		entity = repository.save(entity);
		
		return new ProductDTO(entity);
	}
	
	@Transactional
	public ProductDTO update(Long id,ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDTOToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Id not found "+ id);
		}
	}
	

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Id not found "+ id);
		}catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrityy violation");
		}
		
	}
	
	
	private void copyDTOToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescriptions(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setPrice(dto.getPrice());
		entity.setImgURL(dto.getImg_URL());
		
		entity.getCategories().clear();
		for(CategoryDTO catDTO: dto.getCategories()) {
			Category category = categoryRepsository.getOne(catDTO.getId());
			entity.getCategories().add(category);
		}
		
	}

}
