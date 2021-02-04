package com.shopJpa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.Delivery;
import com.shopJpa.domain.Member;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderItem;
import com.shopJpa.domain.OrderSearch;
import com.shopJpa.domain.item.Item;
import com.shopJpa.repository.ItemRepository;
import com.shopJpa.repository.MemberRepository;
import com.shopJpa.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	
	private final MemberRepository memberRepository;
	
	private final ItemRepository itemRepository;
	
	/**
	 *  1. 주문
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		
		// member entity 조회
		Member member = memberRepository.findOne(memberId);
		
		// item entity 조회
		Item item = itemRepository.findOne(itemId);
		
		// 배송 정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());  // 회원정보에 등록된 주소로 배송한다.
		
		// 주문 상품 생성(여기에서는 주문 상품을 한개로 제약했음 그러나 여러개도 가능)
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		// 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		
		// 주문 저장
		orderRepository.save(order);  // cascade = CascadeType.ALL 속성으로 인해 item, member orderItem, delivery가 다같이 persist 된다.
		
		return order.getId();
	}
	
	/*
	 *  2. 주문 취소
	 */
	@Transactional
	public void cancelOrder(Long orderId) {
		// 주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		
		// 주문 취소
		order.cancel();
	}
	
	// 3. 검색
	public List<Order> searchOrders(OrderSearch orderSearch) {
	//	return orderRepository.findAllByString(orderSearch);
		return orderRepository.findAllByQuerydsl(orderSearch);
	}
	
}

/*
 * 주문 서비스의 주문과 주문 취소 메소드를 보면 비즈니스 로직 대부분이 엔티티에 있다.
 * 서비스 계층은 단순히 엔티티에 필요한 요청들을 위임해주는 역할을 한다.
 * 이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을
 * 도메인 모델 패턴(http://martinfowler.com/eaaCatalog/domainModel.html)이라 한다.
 * 반대로 엔티티에는 비즈니스 로직이 거의 없고, 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을
 * 트랜잭션 스크립트 패턴(http://martinfowler.com/eaaCatalog/transactionScript.html)이라 한다. 
 */
