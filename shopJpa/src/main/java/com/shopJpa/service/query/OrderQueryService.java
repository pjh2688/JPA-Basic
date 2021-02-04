package com.shopJpa.service.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderSearch;
import com.shopJpa.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

	private final OrderRepository orderRepository;
	
	public List<OrderDto> ordersV2() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		
		List<OrderDto> collect = all.stream()
									.map(order -> new OrderDto(order))
									.collect(Collectors.toList());
		
		return collect;
	}
	
}
