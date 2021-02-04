package com.shopJpa.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderSearch;
import com.shopJpa.domain.OrderStatus;
import com.shopJpa.repository.OrderRepository;
import com.shopJpa.repository.order.simplequery.OrderSimpleQueryDto;
import com.shopJpa.repository.order.simplequery.OrderSimpleQueryRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * xToOne(ManyToOne, OneToOne)에서의 성능 최적화.
 * Order
 * Order -> Member
 * Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
	
	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;
	
	// 1-1. 엔티티를 직접 노출(무한루프에 빠진다.) : 해결 방법 : 양방향으로 연관관계가 되어 있는 엔티티는 한쪽에다가  
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		
		// 1-2. 여기까지만 하고 반환시키면 무한루프에 빠진다. -> 양방향 연관관계에서는 양쪽으로 계속 찾아가기때문에 한쪽은 막아줘야 한다.
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		
		// 1-3. 처음에 영속성 컨텍스트에 프록시 객체가 들어있는데 json데이터로 가져올려고 했더니 가져오려는 엔티티 객체가 아니라서 에러를 발생시킨다. -> 해결 방법 : JSON라이브러리 보고 아무것도 하지 말라고 설정 해주면 해결 된다, build.gradle에 추가 implementation 'com.fasterxml.jackson.datatype:jackson-datatype-hibernate5'
		
		// 1-4. 강제 프록시 초기화.
		for(Order order : all) {
			order.getMember().getName();  // order.getMember()까지 -> 프록시 객체
			order.getDelivery().getAddress();  // order.getMember().getName()까지 -> 실제 객체 Lazy 강제 초기화
		}
		
		return all;
	}
	
	// 2. 엔티티를 DTO로 변환 1 - 쿼리 5번 나감 
	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
		
		// 4-1. orderRepository에서 가져온 list orders
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		
		// 4-2 Order를 SimpleOrderDto로 변환하는 방법
		List<SimpleOrderDto> result = orders.stream()  // 4-3. 4-1에서 가져온 orders를 stream으로 돌린다.
											.map(o -> new SimpleOrderDto(o))  // 4-4. 4-3에서 돌린 stream을 map형태로 바꾸고 그 바꾼 데이터를 o라고 이름지어주고 o를 생성자를 통해 SimpleOrderDto로 변환시킨다.
											.collect(Collectors.toList());  // 4-5. 4-4에서 map으로 변환 되어진 걸 collect해서 list로 다시 변환시킨다.
		
		return result;
	}
	
	// 5-1. 엔티티를 DTO로 변환 2 - 페치 조인으로 최적화 : 쿼리 1번 나감, 패치 조인을 하면 지연로딩 설정을 무시한다.
	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		
		// 5-2.  엔티티를 DTO로 변환 2 - 페치 조인으로 최적화 : 첫번째 방법
//		List<SimpleOrderDto> result = new ArrayList<OrderSimpleApiController.SimpleOrderDto>();

//		for(int i = 0; i < orders.size(); i++) {
			
//			result.add(new SimpleOrderDto(orders.get(i)));
			
//		}
		
		// 5-3.  엔티티를 DTO로 변환 2 - 페치 조인으로 최적화 : 두번째 방법(향상된 for문)
		List<SimpleOrderDto> result = new ArrayList<OrderSimpleApiController.SimpleOrderDto>();
		
		for(Order order : orders) {
			result.add(new SimpleOrderDto(order));
		}
		
		// 5-4.  엔티티를 DTO로 변환 3 - 페치 조인으로 최적화 : 세번째 방법(람다식) 
//		List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
		
		return result;
	}
	
	// 6-1. JPA에서 DTO로 바로 조회
	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
	//	return orderRepository.findOrderDtos();  // 6-2. repository는 엔티티를 조회하는 용도로만 사용.
		return orderSimpleQueryRepository.findOrderDtos(); // 6-3. 기존 엔티티를 만지는 repository는 손대지 말고 새로 dto를 만지는 repository를 새로 만들어서 사용한다.
	}
	
	// 3. DTO 생성
	@Data
	static class SimpleOrderDto {
				
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
				
		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();  // LAZY 초기화
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress(); // LAZY 초기화
		}
	}		
	
	
	/*
	 *  * 페치 조인(fetch join)
	 *   - SQL 조인의 종류가 아니다.
	 *   - JPQL[Java persistence Query Language]에서 성능 최적화를 위해 제공하는 기능.
	 *   - 연관된 엔티티(Entity)나 컬렉션(Collection) 객체를 SQL 한 번에 함께 조회해주는 기능.
	 *   - join fetch 명령어 사용
	 *   - 패치 조인 ::= [ LEFT [OUTER] | INNER ] JOIN FETCH 조인경로
	 */
	
	/*
	  	 <엔티티를 DTO로 변환해서 조회> or <DTO로 바로 조회>둘 중 더 나은 것은?
	 *   -권장 하는 방법-
	 *   1. 처음에는 엔티티를 DTO로 변환하는 방법을 선택한다.
	 *   2. 필요하면 패치 조인으로 성능을 최적화 한다. -> 대부분의 성능 이슈가 해결될 것이다.
	 *   3. 그래도 성능이 안나오면 DTO로 직접 조회하는 방법을 사용한다.
	 *   4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
	
	 */
	
	

}
