package com.example.Spring_order.Entity;

public class ProductEntity {
	private Long id;

	private Double price;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductEntity(Long id, Double price, String name) {
		super();
		this.id = id;
		this.price = price;
		this.name = name;
	}

	@Override
	public String toString() {
		return "ProductEntity [id=" + id + ", price=" + price + ", name=" + name + "]";
	}

	public ProductEntity() {
		super();
	}

}
