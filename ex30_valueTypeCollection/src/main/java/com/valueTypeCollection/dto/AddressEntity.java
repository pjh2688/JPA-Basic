package com.valueTypeCollection.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "addressEntity_seq_generator", sequenceName = "addressEntity_seq", allocationSize = 50)
@Table(name = "ADDRESS")  // 
public class AddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressEntity_seq_generator")
	@Column(name = "ADDRESS_ID")
	private Long id;
	
	private Address address;
	
	public AddressEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public AddressEntity(String city, String street, String zipcode) {
		this.address = new Address(city, street, zipcode);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}
