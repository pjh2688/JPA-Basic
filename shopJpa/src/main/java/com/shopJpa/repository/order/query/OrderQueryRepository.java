package com.shopJpa.repository.order.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	// 1. 컬렉션이 제외된 OrderQueryDto를 가져온다. -> dto에는 컬렉션이 있으니 jpql로 가져올댄 제외시켜서 일단 가져온다.
	public List<OrderQueryDto> findOrderDtos() {
		return em.createQuery("select new com.shopJpa.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" +
							" join o.member m" + 
							" join o.delivery d", OrderQueryDto.class).getResultList();
	}
	
	// 3. ToMany 관계는 DB에서 뻥튀기 되기 때문에 나눠서 데이터를 가지고 와야 된다. => N+1 문제 발생
	public List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery("select new com.shopJpa.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" + 
						" from OrderItem oi" + 
						" join oi.item i" + 
						" where oi.order.id = :orderId", OrderItemQueryDto.class)
					.setParameter("orderId", orderId)
					.getResultList();
	}
	
	// 2-1. V4 : ToMany 관계에서 컬렉션 데이터는 DB에서 뻥튀기 되기 때문에 나눠서 데이터를 가지고 와야 된다. - 1 => N+1 문제 발생
	public List<OrderQueryDto> findOrderQueryDtos() {
		
		// 2-2. 컬렉션이 제외된 OrderQueryDto를 가져온다. N번
		List<OrderQueryDto> result = findOrderDtos(); 
		 
		// 2-3. for문, N+1번 -> 주문 1개에 orderItems가 2개이기 때문에 N+1발생.
		for(int i = 0; i < result.size(); i++) {
			 
			List<OrderItemQueryDto> orderItems = findOrderItems(result.get(i).getOrderId());  // 2.4 2-2에서 가져온 OrderQueryDto에서 id를 뽑아와 해당 id의 orderItems를 구해온다. 
			 
			result.get(i).setOrderItems(orderItems);  // 2-5. 구해온 orderItems을 각각 직접 set한다.
			
		}
		
		// 2-6. 람다식 
//		result.forEach(o -> {
//			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
//			o.setOrderItems(orderItems);
//		});
			
		return result;
	
	}
	
	// 3-1. V4 성능 최적화 -> N+1문제 해결.
	public List<OrderQueryDto> findAllOrderQueryDtos() {	
		// 3-2. 컬렉션이 제외된 주문(OrderQueryDto)을 가져온다.
		List<OrderQueryDto> result = findOrderDtos(); 
		
		// 3-3. 주문 id들을 가지고 오는 방법 : 람다식 - java8 이후
//		List<Long> orderIds = result.stream()
//				.map(o -> o.getOrderId())
//				.collect(Collectors.toList());
		
		// 3-4. 주문 id들을 가지고 오는 방법 : 기존 방식 - java8 이전
		List<Long> orderIds = new ArrayList<Long>();  
		
		for(int i = 0; i < result.size(); i++) {
			 
			orderIds.add(i, result.get(i).getOrderId());
		}
		
		// 3-5. 해당 주문 id들을 jpql in 키워드로 차례대로 넣어서 orderItems들을 가져온다.(여기서 orderItems들은 컬렉션임)
		List<OrderItemQueryDto> orderItems = em.createQuery("select new com.shopJpa.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" + 
				" from OrderItem oi" + 
				" join oi.item i" + 
		//		" where oi.order.id = :orderId", OrderItemQueryDto.class)  // 기존 방법
				" where oi.order.id in :orderIds", OrderItemQueryDto.class)
			.setParameter("orderIds", orderIds)
			.getResultList();
		
		// 3-6. 3-5번을 map으로 변환하여 메모리에 올려두어 최적화, 람다식밖에 안됨. for문은 생각해봐야함 -> 잘 모르겠지만 복습해보자.
		Map<Long, List<OrderItemQueryDto>> map = orderItems.stream()
				.collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
		
		// 3-7. map으로만든 id를 통해서 검색 -> 쿼리 수가 3개에서 2개 줄어드는 걸 확인.
		result.forEach(o -> o.setOrderItems(map.get(o.getOrderId())));
		
		return result;
	
	
	}
	
	// 4. 일단 한번에 데이터를 다 가지고 오는 기능
	public List<OrderFlatDto> findAllFlatDtos() {
		return em.createQuery("select new com.shopJpa.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
								" from Order o" + 
								" join o.member m" + 
								" join o.delivery d" + 
								" join o.orderItems oi" +
								" join oi.item i", OrderFlatDto.class).getResultList();
	}
	
}
