package com.inheritanceAssociativeMapping.dto;

import javax.persistence.Entity;

@Entity(name = "book")
public class Book extends Goods {
	
	private String author;
	// 국제표준도서번호[International Standard Book Number, ISBN] : 국제적으로 표준화된 방법에 따라 전세계에서 생산되는 도서에 부여된 고유번호.
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
