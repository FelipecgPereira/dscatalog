package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	private long id;
	private long invalidId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	ProductDTO productDTO ;

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		id = 1L;
		dependentId = 4L;
		invalidId = 999999L;
		productDTO = Factory.createProductDTO();
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		
		when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		when(repository.findById(id)).thenReturn(Optional.of(product));
		
		when(repository.getOne(id)).thenReturn(product);
		
		when(repository.getOne(invalidId)).thenThrow(ResourceNotFoundException.class);
		
		when(categoryRepository.getOne(id)).thenReturn(category);
		
		when(categoryRepository.getOne(invalidId)).thenThrow(ResourceNotFoundException.class);
		
		when(repository.findById(invalidId)).thenReturn(Optional.empty());
		
		doNothing().when(repository).deleteById(id);
		
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(invalidId);
		
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.update(invalidId,productDTO);
		});
	}
	
	
	@Test
	public void upDateShouldBeAbleReturnaProduct() {
	
		ProductDTO result = service.update(id,productDTO);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.findById(invalidId);
		});
	}
	
	
	@Test
	public void findByIdShouldBeAbleReturnaProduct() {
		ProductDTO result = service.findById(id);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		verify(repository, times(1)).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDataIntegrityViolationExceptionWhenIdDoesExists() {
		Assertions.assertThrows(DatabaseException.class,()->{
			service.delete(dependentId);
		});
		
		verify(repository,times(1)).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.delete(invalidId);
		});
		
		verify(repository,times(1)).deleteById(invalidId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(()->{
			service.delete(id);
		});
		
		verify(repository,times(1)).deleteById(id);
	}
	
	
}
