package com.shopJpa.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderItem;
import com.shopJpa.domain.OrderSearch;
import com.shopJpa.domain.OrderStatus;
import com.shopJpa.repository.OrderRepository;
import com.shopJpa.repository.order.query.OrderFlatDto;
import com.shopJpa.repository.order.query.OrderItemQueryDto;
import com.shopJpa.repository.order.query.OrderQueryDto;
import com.shopJpa.repository.order.query.OrderQueryRepository;
import com.shopJpa.service.query.OrderQueryService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	// 핵심 비즈니스 로직으로 엔티티를 찾을 경우 사용
	private final OrderRepository orderRepository;
	
	// 엔티티가 아닌 특정 화면에 맞는 쿼리를 찾을 경우 사용
	private final OrderQueryRepository orderQueryRepository;
	
	//
	private final OrderQueryService orderQueryService;
	
	// 1-1. 엔티티를 조회해서 그대로 반환 : V1 => 주의 : 프록시는 데이터가 없으면 출력을 안해준다.
	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		
		for(Order order : all) {  // 1-2. lazy loading으로 설정 되어 있기 때문에 프록시를 처음에 초기화시켜줘야 한다.
			
			// 1-3. order 프록시 초기화(Lazy 로딩이기 때문)
			order.getMember().getName();
			order.getDelivery().getAddress();
			
			List<OrderItem> orderItems = order.getOrderItems();
			
			// 1-4. orderItem 프록시 초기화(Lazy 로딩이기 때문) 
			for(OrderItem orderItem : orderItems) {
				
				orderItem.getItem().getName();
			}
			
			// 1-5. 람다식 
		//	orderItems.stream().forEach(o -> o.getItem().getName());
			
		}
		
		return all;
	}
	
	// 2-1. 엔티티 조회 후 DTO로 변환 V2 : 기본
	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		
		// 2-7. 프록시 초기화(엔티티는 노출되면 안됨) : Order -> OrderDto로 변환 1 : 람다식
//		List<OrderDto> collect = all.stream()
//									.map(order -> new OrderDto(order))
//									.collect(Collectors.toList());
		
		// 2-8. 프록시 초기화(엔티티는 노출되면 안됨) : Order -> OrderDto로 변환 2 : for문
		List<OrderDto> result = new ArrayList<OrderDto>();
		
		for(int i = 0; i < orders.size(); i++) {
			result.add(new OrderDto(orders.get(i)));
		}
		
		return result;
	}
	
	// 2-2. OrderDto 생성
	@Data
	static class OrderDto {
		
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		
		// 2-3. Dto에 엔티티가 들어가는 경우 값을 못가지고 온다.(프록시를 가지고 오기 때문)
		private List<OrderItemDto> orderItems;
		
		public OrderDto(Order order) {
			// TODO Auto-generated constructor stub
			this.orderId = order.getId();
			this.name = order.getMember().getName();
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery().getAddress();
		
			// 2-4. 엔티티에는 Lazy로딩이 걸려있을 수 있으므로 프록시를 읽게하지 말고 강제로 데이터베이스에서 읽어오게 한다.
			// 원칙적으로 DTO에는 엔티티를 포함시키면 안된다. 왜냐하면 엔티티 스펙이 노출되기 때문이다. 
			// 그래서 dto안에 엔티티가 포함되어야 한다면 그 포함되는 엔티티도 dto로 쪼개줘야 한다.
			// order.getOrderItems().stream().forEach(o -> o.getItem().getName());
			
			// 2-5. 프록시 초기화(엔티티는 노출되면 안됨) : OrderItem -> OrderItemDto로 바꿔주는 방법 1 : dto안에 엔티티는 dto로 쪼개줘야 한다.
//			this.orderItems = order.getOrderItems().stream()
//								.map(orderItem -> new OrderItemDto(orderItem))
//								.collect(Collectors.toList());
			
			// 2-6. 프록시 초기화(엔티티는 노출되면 안됨) : OrderItem -> OrderItemDto로 바꿔주는 방법 2 
			this.orderItems = new ArrayList<OrderItemDto>();
			
			for(int i = 0; i < order.getOrderItems().size(); i++) {
				orderItems.add(new OrderItemDto(order.getOrderItems().get(i)));
			}
		}
	}
	
	// 2-6. OrderItemDto 생성
	@Data
	static class OrderItemDto {
		
		private String itemName;  // 상품 명
		private int orderPrice;  // 주문 가격
		private int count;  // 주문 수량
		
		public OrderItemDto(OrderItem orderItem) {  // 엔티티를 매개변수로 받는다.
			 // 프록시 강제 초기화
			this.itemName = orderItem.getItem().getName(); 
			this.orderPrice = orderItem.getOrderPrice();
			this.count = orderItem.getCount();
		}
		
	}
	
	// 2-7. 엔티티 조회 후 DTO로 변환 V3 : 페치 조인으로 최적화(문제점 : 데이터가 뻥튀기됨, 그러나 distinct 키워드로 해결 가능)
	// * 패치 조인 사용시 단점 : 컬렉션 데이터의 경우 일대다에서는 페이징 불가능. -> 데이터가 실제 DB에서는 뻥튀기 되기 때문.
	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		
		List<Order> orders = orderRepository.findAllWithItem();
		
		// 2-8. 변환 1 : 람다식
//		List<OrderDto> result = orders.stream()
//				.map(o -> new OrderDto(o))
//				.collect(Collectors.toList());
		
		// 2-9. 변환 2 : for문 돌리기
		List<OrderDto> result = new ArrayList<OrderDto>();
		
		for(int i = 0; i < orders.size(); i++) {
			result.add(new OrderDto(orders.get(i)));
		}
		
		return result;
	}
	
	/* 2-10. JPA에서 DTO 직접 조회 V4 - 페이징과 한계 돌파(페이징 가능)
	 *  (1) ToOne(OneToOne, ManyToOne) 관계에 있는 데이터들은 모두 페치조인 한다. ToOne관계는 row수를 증가시키지 않아 페이징 쿼리에 영향을 주지 않는다.
	 *  (2) 컬렉션 데이터는 지연 로딩으로 조회한다.(그냥 lazy loading으로 놔둠)
	 *  (3) 지연 로딩 성능 최적화를 위해 application.properties에 spring.jpa.properties.hibernate.default_batch_fetch_size의 값을 준다.(여기선 500)
	 *  ->  in query를 날려준다. 쿼리를 미리 날려서 데이터를 미리 가져와주는걸 in query라고 한다.
	 */
	@GetMapping("/api/v4/orders")
	public List<OrderDto> ordersV4(
			@RequestParam(value = "offset", defaultValue = "0") int offset,  // offset이란 0부터 시작하는 위치 값.
			@RequestParam(value = "limit", defaultValue = "100") int limit) {
		
		// 2-11. member와 delivery를 포함한 order 엔티티
		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
		
		
		// 2-12. for문 돌리기
		List<OrderDto> result = new ArrayList<OrderDto>();
		
		for(int i = 0; i < orders.size(); i++) {
			result.add(new OrderDto(orders.get(i)));
		}
		
		// 2-13 . 람다식
//		List<OrderDto> result = orders.stream()
//				.map(o -> new OrderDto(o))
//				.collect(Collectors.toList());
		
		return result;
	}
	
	// 2-14. JPA에서 DTO 직접 조회 V5(컬렉션 조회 최적화) - 쿼리 3번(기존 방법)으로 최적화, com.shopJpa.repository.order.query.OrderQueryDto
	@GetMapping("/api/v5/orders")
	public List<OrderQueryDto> ordersV5() {
		return orderQueryRepository.findOrderQueryDtos();  // com.shopJpa.repository.order.query.OrderQueryRepository
		
	}
	
	// 2-15. JPA에서 DTO 직접 조회 V6(컬렉션 조회 최적화) - 쿼리 2번(in 사용)으로 최적화, 일대다 관계인 컬렉션은 IN 절을 활용해서 메모리에 미리 조회하여 최적화 시킨다.
	@GetMapping("/api/v6/orders")
	public List<OrderQueryDto> ordersV6() {
		return orderQueryRepository.findAllOrderQueryDtos();
			
	}	
	
	// 2-16. JPA에서 DTO 직접 조회 V7(플랫 데이터 최적화) : 쿼리 1번으로 최적화, JOIN 결과를 그대로 조회 후(한방) 애플리케이션에서 원하는 모양(OrderFlatDto)으로 직접 변환하여 반환(중복을 걸러내지 못함) 그러나 페이징은 가능
	@GetMapping("/api/v7/orders")
	public List<OrderFlatDto> ordersV7() {
		return orderQueryRepository.findAllFlatDtos();
			
	}	
	
	// 2-17. JPA에서 DTO 직접 조회 V8(플랫 데이터 최적화) -> API 스팩(OrderQueryDto)에 맞추기 위한 방법, 단점 : 페이징 불가능, 애플리케이션에서 추가 작업 필요
	@GetMapping("/api/v8/orders")
	public List<OrderQueryDto> ordersV8() {
	
		List<OrderFlatDto> flats = orderQueryRepository.findAllFlatDtos();
		
		return flats.stream()
				.collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
						Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), Collectors.toList())
						)).entrySet().stream()
						.map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue() ))  // stream<OrderQueryDto>
						.collect(Collectors.toList()); 
						

	}
	
	// 2-18. OSIV[Open Session In View]와 성능 최적화 : service 계층에서 동작 v2 변형버전 (트랜잭션이 걸려있는 계층 안에서 동작), spring.jpa.open-in-view=false로 설정했을 경우 지연로딩이 컨트롤러 계층에서는 적용이 안된다.
	@GetMapping("/api/v9/orders")
	public List<com.shopJpa.service.query.OrderDto> ordersV9() {
		return orderQueryService.ordersV2();
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
 	*ORM(Object-Relational Mapping)
    -> DB를 java Object에 mapping 하여,사실상 SQL방식을 통하지 않고 java Object를 다루어 DB 작업을 대행하는 것.
 */
