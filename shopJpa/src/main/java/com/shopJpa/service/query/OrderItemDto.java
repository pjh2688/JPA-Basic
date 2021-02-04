package com.shopJpa.service.query;


import com.shopJpa.domain.OrderItem;

import lombok.Data;

@Data
public class OrderItemDto {

	private String itemName;  // 상품 명
	private int orderPrice;  // 주문 가격
	private int count;  // 주문 수량
	
	public OrderItemDto(OrderItem orderItem) {
		// 프록시 강제 초기화
		this.itemName = orderItem.getItem().getName(); 
		this.orderPrice = orderItem.getOrderPrice();
		this.count = orderItem.getCount();
	}
}
