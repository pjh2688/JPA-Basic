package com.shopJpa.domain;

import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class Address {

	private String city;
	private String street;
	private String zipcode;
	
	// protected로
	protected Address() {
		// TODO Auto-generated constructor stub
	}
	// setter를 안만들고 생성자로 초기화.
	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}
}
