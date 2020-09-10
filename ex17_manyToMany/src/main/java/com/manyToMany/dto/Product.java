package com.manyToMany.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "product")
@SequenceGenerator(name = "product_seq_generator", sequenceName = "product_seq", allocationSize = 50)
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_generator")
	@Column(name = "PRODUCT_ID")
	private Long id;
	
	@Column(name = "PRODUCT_NAME")
	private String name;
	
//	@ManyToMany(mappedBy = "products")
//	private List<Customer> customers = new ArrayList<Customer>();
	
	@OneToMany(mappedBy = "product")
	private List<CustomerProduct> customerProducts = new ArrayList<CustomerProduct>();
	
	public Product() {
		// TODO Auto-generated constructor stub
	}
	
//	public List<Customer> getCustomers() {
//		return customers;
//	}
//
//	public void setCustomers(List<Customer> customers) {
//		this.customers = customers;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
