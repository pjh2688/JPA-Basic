package com.polymorphismQuery.dto;

//import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("BB")  // 생략하면 클래스 명이 들어감.
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
