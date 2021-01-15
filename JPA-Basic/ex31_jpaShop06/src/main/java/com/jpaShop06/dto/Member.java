package com.jpaShop06.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Member extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "MEMBER_ID")
	private Long id;
	
	@Column(name = "MEMBER_NAME")
	private String name;
	
	@Embedded
	private Address address;
	
	@SuppressWarnings("unused")
	private Address getAddress() {
		return address;
	}

	@SuppressWarnings("unused")
	private void setAddress(Address address) {
		this.address = address;
	}

	// 양방향 매핑 설정
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<Order>();
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Member() {
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

	
}
