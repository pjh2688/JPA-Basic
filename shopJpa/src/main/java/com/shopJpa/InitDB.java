package com.shopJpa;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.Delivery;
import com.shopJpa.domain.Member;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderItem;
import com.shopJpa.domain.item.Book;

import lombok.RequiredArgsConstructor;

/**
 *  Application 시작 시점에 초기 데이터를 세팅해주는 클래스 
 *  주문 1. userA
 *   - JPA1 BOOK
 *   - JPA2 BOOk
 *  주문 2. userB
 *   - SPRING1 BOOK
 *   - SPRING2 BOOK
 */  
@Component  // 2. @Component : Component scan, XML에 일일이 하나하나 빈(bean)을 등록 하지 않고 각 클래스에 @Component를 붙여  자동으로 빈(bean)으로 등록해주는 어노테이션.
@RequiredArgsConstructor  // 3.
public class InitDB {  // 1. 조회용 샘플 데이터 입력용
	
	private final InitService initService;
	
	@PostConstruct  // 5. @PostConstruct : 객체가 생성된 후 별도의 초기화 작업을 위한 어노테이션, 이 어노테이션을 설정해놓은 메소드는 WAS가 띄워질 때 같이 실행된다.
	public void init() {
		
		// 8. userA, userB init
		initService.dbInit1();
		initService.dbInit2();
	}

	@Component  
	@Transactional
	@RequiredArgsConstructor
	static class InitService {  // 4.
		
		private final EntityManager em;
		
		// 6. userA data
		public void dbInit1() {
			
			// 회원 생성
			Member member = createMember("userA", "청주시 상당구", "산성로 55", "28720");
			em.persist(member);
			
			// 상품 등록
			Book book1 = createBook("JPA1 BOOK", 10000, 100);
			em.persist(book1);
			
			Book book2 = createBook("JPA2 BOOK", 20000, 100);
			em.persist(book2);
			
			// 주문 목록 생성
			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
			
			// 주문 배송지 설정
			Delivery delivery = createDelivery(member);
			
			// 주문 생성
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
			
		}
		
		// 7. userB data
		public void dbInit2() {
			
			// 회원 생성
			Member member = createMember("userB", "성남시 분당구", "장미로 100", "13505");
			em.persist(member);
			
			// 상품 등록
			Book book1 = createBook("SPRING1 BOOK", 20000, 200);
			em.persist(book1);
			
			Book book2 = createBook("SPRING2 BOOK", 40000, 300);
			em.persist(book2);
			
			// 주문 목록 생성
			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
			
			// 주문 배송지 설정
			Delivery delivery = createDelivery(member);
			
			// 주문 생성
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
			
		}
		
		// Member 생성 메소드
		private Member createMember(String id, String city, String street, String zipcode) {
			
			Member member = new Member();
			member.setName(id);
			member.setAddress(new Address(city, street, zipcode));
			
			return member;
		}
		
		// Book 생성 메소드
		private Book createBook(String name, int price, int stockQuantity) {
			
			Book book = new Book();
			book.setName(name);
			book.setPrice(price);
			book.setStockQuantity(stockQuantity);
			
			return book;
		}
		
		// Delivery 생성 메소드
		private Delivery createDelivery(Member member) {
			
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			return delivery;
		}
	}
}
