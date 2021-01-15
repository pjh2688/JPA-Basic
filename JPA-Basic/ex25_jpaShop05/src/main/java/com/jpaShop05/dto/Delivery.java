package com.jpaShop05.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Delivery extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)  // 하이버네이트 시퀀스 적용.
	@Column(name = "DELIVERY_ID")
	private Long id;
	
	private String city;
	private String street;
	private String zipcode;
	private DeliveryStatus status;
	
	// 양방향 매핑(delivery : order)
	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)  // @OneToOne은 기본이 Eager 그래서 LAZY로 설정해줘야한다.
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public DeliveryStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryStatus status) {
		this.status = status;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	  
}
