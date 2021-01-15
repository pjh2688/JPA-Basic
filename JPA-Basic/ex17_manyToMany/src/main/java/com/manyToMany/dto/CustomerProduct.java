package com.manyToMany.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "cp_seq_generator", sequenceName = "cp_seq", allocationSize = 50)
public class CustomerProduct {  // Orders

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cp_seq_generator")
	@Column(name = "CP_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	
	// 데이터 추가.
	private int orderAmount;
	
	private LocalDateTime orderDateTime;
	
	public CustomerProduct() {
		// TODO Auto-generated constructor stub
	}
	
	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public LocalDateTime getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
