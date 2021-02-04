package com.jpaShop.dto;

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
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@SequenceGenerator(name = "delivery_seq_generator", sequenceName = "delivery_seq", allocationSize = 50)
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq_generator")
	@Column(name = "DELIVERY_ID")
	private Long id;
	
	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)  // 3. 연관 관계 매핑(1:1) // 4. 읽기 전용. // 5. 일대일 -> fetch = FetchType.LAZY
	private Order order;
	
	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)  // 2. @Enumerated(EnumType.STRING)으로 기본이 ORDINAL(이건 숫자로 표시)
	private DeliveryStatus status;  // 1. 배송 상태[READY, COMP] -> ENUM 타입
}
