package com.shopJpa.form;

import lombok.Setter;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter @Setter
public class MemberForm {  // 폼 객체 : DTO

	@NotEmpty(message = "회원 이름은 필수 입니다.") // 1. springboot validation 의존성 추가해야 사용 가능, 뷰페이지에서 ${#fields.hasErrors('name')} 이렇게 사용한다.
	private String name;
	
	private String city;
	private String street;
	private String zipcode;
}
