package com.jpaShop05.dto;

import javax.persistence.Entity;

@Entity
public class Book extends Item { // 상속 관계 매핑
	
	private String author;
	private String isbn;
	
	public Book() {
		// TODO Auto-generated constructor stub
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	
}
