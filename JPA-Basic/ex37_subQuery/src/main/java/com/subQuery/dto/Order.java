package com.subQuery.dto;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "order")
@SequenceGenerator(name = "order_seq_generator", sequenceName = "order_seq", allocationSize = 50)
@Table(name = "ORDERS")  // orders라고 데이터베이스에선 사용하자. order는 예약어이기 때문에 오라클에선 생성 안된다.
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_generator")
	@Column(name = "ORDER_ID")
	private Long id;
	
	@Column(name = "ORDER_AMOUNT")
	private int orderAmount;
	
	@Embedded
	private Address address;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public Order() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}


	
}
