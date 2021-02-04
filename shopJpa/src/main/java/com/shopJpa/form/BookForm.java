package com.shopJpa.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

	/* 1. Item 공통 속성 */
	private Long id;
	private String name;
	private int price;
	private int stockQuantity;
	
	/* 2. Book 속성 */
	private String author;
	private String isbn;
	
}
