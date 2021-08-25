package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository repository;
	private long idExist ;
	private long idNoExist;
	private long contTotalProduct;
	
	@BeforeEach
	void setUp() throws Exception {
		idExist = 1L;
		idNoExist = 999999L;
		contTotalProduct= 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(contTotalProduct+1, product.getId());
		
	}
	
	@Test
	public void findByIdSholdReturnProductById() {
		Optional<Product> result = repository.findById(idExist);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdNoExistSholdhowEmptyResultDataAccessExceptionWhenDoesNotExist() {
		Optional<Product> result = repository.findById(idNoExist);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

		repository.deleteById(idExist);
		
		Optional<Product> result  = repository.findById(idExist);
		Assertions.assertFalse(result.isPresent());
	}
	
	
	
	@Test
	public void deleteShouldThowEmptyResultDataAccessExceptionWhenDoesNotExist() {
			
			Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
				repository.deleteById(idNoExist);
			});
	}
	
}
