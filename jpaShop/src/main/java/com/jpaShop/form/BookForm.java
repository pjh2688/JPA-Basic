package com.jpaShop.form;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

	private Long id;
	
	// 1. 책과 관련된 공통 속성
	@NotEmpty(message = "책 이름은 필수 입니다.")
	private String name;
	
	private int price;
	
	private int stockQuantity;
	
	// 2. 책과 관련된 개별 속성
	private String author;
	
	private String isbn;
}
