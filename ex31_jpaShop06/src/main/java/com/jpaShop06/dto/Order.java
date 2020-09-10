package com.jpaShop06.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")  // orders라고 데이터베이스에선 사용하자.
public class Order extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)  // 하이버네이트 시퀀스 적용.
	@Column(name = "ORDER_ID")
	private Long id;
	
	// 단방향 매핑 2
	@ManyToOne(fetch = FetchType.LAZY)  // 지연로딩 설정.
	@JoinColumn(name = "MEMBER_ID")  // 외래 키 
	private Member member;
	
	// 양방향 매핑 설정
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 지연 로딩, 영속선 전이 설정.
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING)  //  ORDINAL X
	private OrderStatus status;
	
	// 1:1 관계 매핑(delivery : order)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // 지연 로딩, 영속선 전이 설정.
	@JoinColumn(name = "DELIVERY_ID")
	private Delivery delivery;
	
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);  // 새로 추가된 order를 받아 orderItems List객체에 추가하고
		orderItem.setOrder(this);  // 현재 나의 order를 넣어줘서 양방향 연관관계가 걸리도록 설정.
	}
	
	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	//  단방향 매핑 2 getter/setter	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	
}
