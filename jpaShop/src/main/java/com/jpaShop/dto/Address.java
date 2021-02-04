package com.jpaShop.dto;

import javax.persistence.Embeddable;

import lombok.Getter;

// 1. 값 타입은 변경 불가능하게 설계해야 한다.
@Embeddable  // JPA 내장 타입
@Getter
public class Address {

	private String city;
	private String street;
	private String zipcode;
	
	// 2. JPA 스펙상 엔티티나 임베디드 타입(@Embeddable)은 자바 기본 생성자를
	// 3. 기본 생성자는 public 또는 protected로 설정해야 되는데 값 타입에서는 public 보다는 protected로 설정해놓는게 안전하다
	protected Address() {
		// TODO Auto-generated constructor stub
	}
	
	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}
}
