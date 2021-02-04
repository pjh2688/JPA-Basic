package com.shopJpa.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long id;
	
	@JsonIgnore // 양방향 연관관계에서는 한쪽을 @JsonIgnore 꼭 달아줘야 한다.
	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
	private Order order;
	
	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)  // ENUM 타입은 @Enumerated(EnumType.STRING) -> 중간에 값이 추가될 수가 있으니 꼭 EnumType.STRING으로
	private DeliveryStatus status;  // READY, COMP

}
