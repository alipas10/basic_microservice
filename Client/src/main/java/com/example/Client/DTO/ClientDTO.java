package com.example.Client.DTO;

public class ClientDTO {

	private Long id;

	private String name;

	private String email;

	private Long productId;

	public ClientDTO() {
		super();
	}

	public ClientDTO(Long id, String name, String email, Long productId) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.productId = productId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
