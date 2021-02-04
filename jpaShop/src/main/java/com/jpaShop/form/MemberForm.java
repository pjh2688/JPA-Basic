package com.jpaShop.form;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

// 회원 가입 폼 객체
@Getter	@Setter
public class MemberForm {

	@NotEmpty(message = "회원 이름은 필수 입니다.")
	private String name;
	
	private String city;
	private String street;
	private String zipcode;
}
