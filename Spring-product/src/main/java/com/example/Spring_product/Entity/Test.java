package com.example.Spring_product.Entity;

public class Test {

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Test [content=" + content + "]";
	}

	public Test(String content) {
		super();
		this.content = content;
	}

	public Test() {
		super();
	}

}
