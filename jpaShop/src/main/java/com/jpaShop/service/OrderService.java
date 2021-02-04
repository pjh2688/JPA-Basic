package com.jpaShop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpaShop.dao.ItemRepository;
import com.jpaShop.dao.MemberRepository;
import com.jpaShop.dao.OrderRepository;
import com.jpaShop.dto.Delivery;
import com.jpaShop.dto.Item;
import com.jpaShop.dto.Member;
import com.jpaShop.dto.Order;
import com.jpaShop.dto.OrderItem;
import com.jpaShop.dto.OrderSearch;

import lombok.RequiredArgsConstructor;

@Service  // 1.  
@Transactional(readOnly = true)  // 2. 성능 최적화(readOnly를 true로  전체 적용), org.springframework.transaction.annotation.Transactional
@RequiredArgsConstructor  // 3. final이 붙은 변수만 생성자를 생성해준다.(lombok 기능)
public class OrderService {

	private final OrderRepository orderRepository;
	
	private final MemberRepository memberRepository;  // 6. 주문(order)에서 매개변수 memberId로 해당 Member 데이터를 꺼내올 MemberRepository가 필요.
	
	private final ItemRepository itemRepository;  // 7. 또 주문(order)를 할 때 매개변수 itemId로 주문할 Item 데이터를 꺼내올 ItemRepository가 필요.
	
	/**
	 * 주문 하기
	 */ 
	@Transactional(readOnly = false)  // 5. 주문(order)는 데이터를 변경하는 작업이기 때문에 OrderService 전체가 @Transactional 옵션이 true로 읽기만 가능하므로 주문 서비스는 @Transactional을 false로 설정해주어(기본 값이 false) 읽기, 쓰기 모두 가능하게 해준다.
	public Long order(Long memberId, Long itemId, int count) {  // 4. 주문(Order)를 하기 위해선 memberId, itemId, count 3개의 값이 필요하기 때문에 매개변수로 받는다.
		
		// 8. 전달된 memberId로 Member 엔티티 조회
		Member member = memberRepository.find(memberId);
		
		// 9. 전달된 itemId로 해당 Item 엔티티 조회
		Item item = itemRepository.find(itemId);
		
		// 10. 배송 정보 생성(여기선 회원의 주소를 바로 사용했지만 실제로는 입력해준 값을 사용해야 한다.)
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		
		// 11. 주문 상품 생성(OrderItem의 주문 생성 메소드 사용)
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		// 12. 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		
		// 13. 주문 저장
		orderRepository.save(order);  // 14. order만 persist해도 위에서 생성한 delivery나 orderItem도 같이 persist 되는 이유는 Order 엔티티에 해당 엔티티들의 맵핑 옵션을 cascade = CascadeType.ALL로 해줘서 연쇄적으로 persist 되기 때문이다.
									  // cascade = CascadeType.ALL 옵션이 설정 안되어 있으면 persist를 각각 다 해줘야 한다.
		return order.getId();
	}
	
	/**
	 * 주문 취소 하기
	 */ 
	@Transactional(readOnly = false)  // 16. 주문 취소할땐 읽기, 쓰기 모두 가능해야 한다.
	public void cancelOrder(Long orderId) {  // 15. 주문 취소 메소드
		
		Order order = orderRepository.find(orderId);// 17. orderId를 통해 orderRepository에서 해당 주문(order)을 찾는다.
		
		order.cancel();  // 18. 취소.
	}
	
	/**
	 * 주문 검색 하기
	 */ 
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByString(orderSearch);
	}
}

/*
 	1. 컨트롤러(Controller)는 서비스(Service)에게 특정 업무를 요청하고,
 	2. 서비스(Service)는 업무를 수행하기 위해 필요한 일을 DAO에게 위임하거나, 업무를 수행하고 나온 자료(Data)를 DAO를 통해 저장한다.
    3. DAO는 "데이터에 직접적으로 접근하는 역할", Service는 "비즈니스 로직을 수행하는 역할"
    4. 추가로 Service를 Interface로 만들어서 implements해서 사용하는 이유는
            첫번째로는 Controller에서 Service로 작업 요청을 할 때 사용자가 그 Service를 제공하는 사람이 그 일을 어떤 방식으로 처리하는지는 알 필요가 없기 때문이다.
            두번째로는 예를 들어 은행원에는 신한은행원도 있고 농협은행원도 있는 것처럼 다양한 은행이 있다.
            그렇기 때문에 공통으로 쓸 은행원이라는 인터페이스를 만들어 주고 사용한다면 만들어진 은행원을 신한은행에서 농협은행으로만 바꿔주면 프로그램 수정이 끝난다. 
    5. 컨트롤러는 요청(request) 값을 받아서 서비스단으로 던져주는 역할과 view 단과 연결해주는 역할로 사용한다.
    6. 데이터 가공은 서비스(Service)단에서 하고 DB에서 데이터를 호출하는 역할은 DAO에서 한다.
    7. DAO들을  최대한 쪼개는게 나중에 재사용성을 높일 수 있다.
    8. Service는 여러 DAO들을 조립하는 것이다.
    
    9. 주문 서비스(OrderService)의 주문(order)과 주문 취소(cancelOrder) 메소드를 보면 비즈니스 로직 대부분이 엔티티(DAO or Repository) 쪽에 있다.
   10. 서비스 계층은 단순히 엔티티(DAO or Repository)에다가 필요한 데이터들을 처리할 작업 요청을 위임하는 역할을 한다. 
            이처럼 엔티티(DAO or Repository)가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이라고 한다.
   11. 반대로 엔티티(DAO, or Repository)에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분 비즈니스 로직을 처리하는 것을 트랜잭션 스크립트 패턴이라고 한다.         
 
 */
 