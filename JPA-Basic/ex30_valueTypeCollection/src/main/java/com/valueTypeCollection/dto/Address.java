package com.valueTypeCollection.dto;

import java.util.Objects;

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
	

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setZipcode(String zipcode) {
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
