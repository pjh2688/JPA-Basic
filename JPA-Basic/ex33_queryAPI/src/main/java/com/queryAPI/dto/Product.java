package com.queryAPI.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	@Column(name = "PRODUCT_PRICE")
	private int price;
	
	@Column(name = "PRODUCT_STOCKAMOUNT")
	private int stockAmount;
	
	public Product() {
		// TODO Auto-generated constructor stub
	}

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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(int stockAmount) {
		this.stockAmount = stockAmount;
	}
	
	

}
