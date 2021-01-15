package com.valueTypeANDimmutableObject.dto;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

	private String city;
	private String street;
	private String zipcode;
	
	public Address() {
		// TODO Auto-generated constructor stub
	}
	
	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}
	
	public String getStreet() {
		return street;
	}

	public String getZipcode() {
		return zipcode;
	}

	// 불변 객체로 만들기 위해선 public을 private 바꿔줘서 접근 못하게 하거나 아에 setter를 지워주면 불변 객체 스타일로 만들어진다.
//	public void setCity(String city) {
//		this.city = city;
//	}
//
//	public void setStreet(String street) {
//		this.street = street;
//	}
//
//	public void setZipcode(String zipcode) {
//		this.zipcode = zipcode;
//	}
	
	
}
