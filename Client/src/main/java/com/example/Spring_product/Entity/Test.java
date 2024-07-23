package com.example.Spring_product.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Test {

	@JsonProperty(value = "content")
	private String contentMessge;

	public String getContentMessge() {
		return contentMessge;
	}

	public void setContentMessge(String contentMessge) {
		this.contentMessge = contentMessge;
	}

	public Test(String contentMessge) {
		super();
		this.contentMessge = contentMessge;
	}

	public Test() {
		super();
	}

	@Override
	public String toString() {
		return "Test [contentMessge=" + contentMessge + "]";
	}

}
