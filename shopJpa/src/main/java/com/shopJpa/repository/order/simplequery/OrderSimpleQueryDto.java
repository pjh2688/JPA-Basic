package com.shopJpa.repository.order.simplequery;

import java.time.LocalDateTime;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.OrderStatus;

import lombok.Data;

//3. DTO 생성
@Data
public class OrderSimpleQueryDto {

	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;

	public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
		this.orderId = orderId;
		this.name = name; // LAZY 초기화
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address; // LAZY 초기화
	}
}