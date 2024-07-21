package com.example.Spring_product.Entity;

public class ProductEntity {

	private Long productId;

	private Double price;

	private String name;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public ProductEntity(Long productId, Double price, String name) {
		super();
		this.productId = productId;
		this.price = price;
		this.name = name;
	}

	public ProductEntity() {
		super();
	}

	@Override
	public String toString() {
		return "ProductEntity [productId=" + productId + ", price=" + price + ", name=" + name + "]";
	}

}
