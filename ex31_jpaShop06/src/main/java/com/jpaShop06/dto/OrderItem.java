package com.jpaShop06.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ORDER_ITEM_ID")
	private Long id;

//  단방향 매핑(orderId) 2(참조를 사용하도록 변경)	
	@ManyToOne(fetch = FetchType.LAZY)  // 지연로딩 설정
	@JoinColumn(name = "ORDER_ID")
	private Order order;
	
//  단방향 매핑(itemId) 2(참조를 사용하도록 변경)	
	@ManyToOne(fetch = FetchType.LAZY)  // 지연로딩 설정
	@JoinColumn(name = "ITEM_ID")
	private Item item;
	
	private int orderPrice;
	private int count;
	
	public OrderItem() {
		// TODO Auto-generated constructor stub
	}
	
	// 단방향 매핑(orderId) 2 getter/setter	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	// 단방향 매핑(itemId) 2 getter/setter	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
