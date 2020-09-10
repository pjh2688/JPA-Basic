package com.jpaShop06.dto;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {
	
	@Column(length = 10)
	private String city;
	
	@Column(length = 20)
	private String street;
	
	@Column(length = 5)
	private String zipcode;
	
	public String fullAddress() {
		return getCity() + " " + getStreet() + " " + getZipcode();
	}
	
	public Address() {
		// TODO Auto-generated constructor stub
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

	// setter는 private으로 
	@SuppressWarnings("unused")
	private void setCity(String city) {
		this.city = city;
	}

	@SuppressWarnings("unused")
	private void setStreet(String street) {
		this.street = street;
	}

	@SuppressWarnings("unused")
	private void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	// equals 재정의(중요)
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Address address = (Address) obj;
		return Objects.equals(city, address.getCity()) && 
				Objects.equals(street, address.getStreet()) &&
				Objects.equals(zipcode, address.getZipcode());
	}

	// equals 메소드 재정의 후 hashCode 메소드도 재정의
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(getCity(), getStreet(), getZipcode());
	}
	
	
}
