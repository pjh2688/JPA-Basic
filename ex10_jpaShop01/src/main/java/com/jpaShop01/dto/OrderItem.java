package com.jpaShop01.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ORDER_ITEM_ID")
	private Long id;
	
//  단방향 매핑(orderId) 1	
	@Column(name = "ORDER_ID")
	private Long orderId;
	
//	단방향 매핑(itemId) 1
	@Column(name = "ITEM_ID")
	private Long itemId;
	
	private int orderPrice;
	private int count;
	
	public OrderItem() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
//  단방향 매핑(orderId) 1 getter/setter
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

//  단방향 매핑(itemId) 1 getter/setter
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public int getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
