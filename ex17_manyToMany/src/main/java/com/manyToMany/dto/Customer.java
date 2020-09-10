package com.manyToMany.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.JoinTable;
// import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "customer")
@SequenceGenerator(name = "customer_seq_generator", sequenceName = "customer_seq", allocationSize = 50)
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_generator")
	@Column(name = "CUSTOMER_ID")
	private Long id;
	
	@Column(name = "CUSTOMER_NAME")
	private String name;
	
//	// 다대다 매핑(이런 방식은 안쓴다.)
//	@ManyToMany
//	@JoinTable(name = "CUSTOMER_PRODUCT")  // 다대다시 중간 연결 테이블
//	private List<Product> products = new ArrayList<Product>();
	
	@OneToMany(mappedBy = "customer")
	private List<CustomerProduct> customerProducts = new ArrayList<CustomerProduct>();
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}

//	public List<Product> getProducts() {
//		return products;
//	}
//
//	public void setProducts(List<Product> products) {
//		this.products = products;
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
