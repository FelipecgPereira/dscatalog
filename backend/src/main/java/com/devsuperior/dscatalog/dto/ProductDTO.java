package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Long id;
	@Size(min = 5, max = 60,message = "Deve ter entre 5 a 60 caracteres")
	@NotBlank(message = "Campo obrigatorio")
	private String name;
	private String description;
	@Positive(message = "Preço deve ser um valor positivo")
	private Double price;
	private String img_URL;
	@PastOrPresent(message = "Data invalida")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	
	public ProductDTO() {}


	public ProductDTO(Long id, String name, String description, Double price, String img_URL, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.img_URL = img_URL;
		this.date = date;
	}
	
	public ProductDTO(Product entity) {
		super();
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescriptions();
		this.price = entity.getPrice();
		this.img_URL = entity.getImgURL();
		this.date = entity.getDate();
	}
	
	public ProductDTO(Product entity, Set<Category>categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public String getImg_URL() {
		return img_URL;
	}


	public void setImg_URL(String img_URL) {
		this.img_URL = img_URL;
	}


	public Instant getDate() {
		return date;
	}


	public void setDate(Instant date) {
		this.date = date;
	}


	public List<CategoryDTO> getCategories() {
		return categories;
	}


	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}
	
	
}
