package com.shopJpa.service.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderDto {
	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	
	// 2-4. Dto에 엔티티가 들어가는 경우 값을 못가지고 온다.(프록시를 가지고 오기 때문)
	private List<OrderItemDto> orderItems;
	
	public OrderDto(Order order) {
		// TODO Auto-generated constructor stub
		this.orderId = order.getId();
		this.name = order.getMember().getName();
		this.orderDate = order.getOrderDate();
		this.orderStatus = order.getStatus();
		this.address = order.getDelivery().getAddress();
	
		// 2-5. 엔티티에는 Lazy로딩이 걸려있을 수 있으므로 프록시를 읽게하지 말고 강제로 데이터베이스에서 읽어오게 한다.
		// 원칙적으로 DTO에는 엔티티를 포함시키면 안된다. 왜냐하면 엔티티 스펙이 노출되기 때문이다. 
		// 그래서 dto안에 엔티티가 포함되어야 한다면 그 포함되는 엔티티도 dto로 쪼개줘야 한다.
	//	order.getOrderItems().stream().forEach(o -> o.getItem().getName());
		
		// 2-3. 엔티티
		this.orderItems = order.getOrderItems().stream()
							.map(orderItem -> new OrderItemDto(orderItem))
							.collect(Collectors.toList());
	}
}
