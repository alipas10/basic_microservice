package com.example.Spring_product.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "products")
@Entity
public class ProductEntity {

	@Id
	private Long id;

	@Column
	private Long price;

	@Column
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductEntity(Long id, Long price, String name) {
		super();
		this.id = id;
		this.price = price;
		this.name = name;
	}

	public ProductEntity() {
		super();
	}

	@Override
	public String toString() {
		return "ProductEntity [id=" + id + ", price=" + price + ", name=" + name + "]";
	}
	
	

}
