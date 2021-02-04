package com.shopJpa.repository.order.query;

import java.time.LocalDateTime;
import java.util.List;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.OrderStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "orderId")  // 람다식으로 collect해서 groupingBy할때 orderId를 기준으로  중복되는걸 하나로 묶어준다.(lombok) -> 기준점을 설정해주는 것.
public class OrderQueryDto {
	
	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	
	private List<OrderItemQueryDto> orderItems;

	// 기본
	public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;

	}

	// 컬렉션 포함
	public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, List<OrderItemQueryDto> orderItems) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		this.orderItems = orderItems;
	}
	
	
}
