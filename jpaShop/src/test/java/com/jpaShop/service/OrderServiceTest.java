package com.jpaShop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;  // 8. organize imports -> 테스트 케이스 생성 후 경고창 제거
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jpaShop.dao.MemberRepository;
import com.jpaShop.dao.OrderRepository;
import com.jpaShop.dto.Address;
import com.jpaShop.dto.Item;
import com.jpaShop.dto.Member;
import com.jpaShop.dto.Order;
import com.jpaShop.dto.OrderStatus;
import com.jpaShop.dto.item.Book;
import com.jpaShop.exception.NotEnoughStockException;

@RunWith(SpringRunner.class)  // 1. JUNIT에게 테스트를 할거라고 알려주는 어노테이션 1
@SpringBootTest  // 2. JUNIT에게  테스트를 할거라고 알려주는 어노테이션 2 -> 1,2 두개가 모두 있어야 springboot 프로젝트랑 integration 해서 테스트를 실행할 수 있다.
@Transactional  // 3. 트랜잭션 걸어주고 테스트가 끝나면 Rollback을 해준다. (테스트 패키지안에서)
public class OrderServiceTest {

	// 10.
	@Autowired  
	EntityManager em;
	
	// 14. 
	@Autowired
	OrderService orderService;
	
	// 17.
	@Autowired
	OrderRepository orderRepository;
	
	// 18.
	@Autowired
	MemberRepository memberRepository;
	
	@Test  // 7. @Test 어노테이션을 이용해 테스트 기능 작성시 import static org.junit.Assert.assertEquals; 쪽에 경고창이 뜨는데 
	public void 상품주문() throws Exception {  // 4. 상품주문 테스트 기능 틀 만들기 1
		
		// given : 이런게 주어졌을 때
				
		// 9. 주문이 들어올 때 회원의 정보를 받고
		Member member = new Member();
		member.setName("Park Jong Hee");
		member.setAddress(new Address("충청북도 청주시", "상당구 산성로 55", "LH@ 104-1602"));
	
		// 10. 영속성 컨텍스트에 저장
		em.persist(member);
		
		// 11. 주문자가 주문한 파피용이라는 책의 가격과 재고 수량을 가지고 오고
		Book book = new Book();
		book.setName("파피용");
		book.setPrice(10000);
		book.setStockQuantity(10);
		
		// 12. 회원이 주문한 물품의 수량을 가지고 온 뒤 
		int orderCount = 2;
		
		// 13. 영속성 컨텍스트에 저장.
		em.persist(book);

		// when : 이런게 실행 되면
		
		// 15. 회원의 id와, 회원이 주문한 물품 id, 회원이 주문한 물품의 수량을 가지고 주문.
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
		
		// then : 이런 결과가 나온다.
		
		// 16. 15번으로 주문이 들어오면 orderRepository에서 해당 주문 정보를 찾아온다
		Order getOrder = orderRepository.find(orderId);
		
		em.flush();
		assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("주문한 상품의 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
		assertEquals("주문 가격은 가격 * 수량이다. ", getOrder.getOrderItems().get(0).getItem().getPrice() * orderCount, getOrder.getTotalPrice());
		assertEquals("주문 수량만큼  재고에서 줄어야 한다.", 8, getOrder.getOrderItems().get(0).getItem().getStockQuantity());
	}
	
	@Test(expected = NotEnoughStockException.class) 
	public void 상품주문_재고수량초과() throws Exception {  // 5. 상품주문_재고수량초과 테스트 기능 틀 만들기 2
		// given : 이런게 주어졌을 때
		
		// 19. 영속성 컨텍스트에서 Member 엔티티 조회
		Member member = createMember();
		
		// 20. DB에서 가격이랑 재고, 상품명 불러와서 주문 상품을 만든다.
		Item item = createBook("파피용", 10000, 10);
		
		// 21. 주문 수량을 받아 온다.
		int orderCount = 11;
		
		// when : 이런게 실행 되면
		orderService.order(member.getId(), item.getId(), orderCount);
		
		// then : 이런 결과가 나온다.
		fail("재고 수량 부족 예외가 발생해야 한다.");
		
	}
	
	@Test
	public void 주문취소() throws Exception {  // 6. 주문취소 테스트 기능 틀 만들기 3
		// given : 이런게 주어졌을 때
		Member member = createMember();
		Item item = createBook("파피용", 10000, 10);
		
		int orderCount = 2;
		
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		
		// when : 이런게 실행 되면
		orderService.cancelOrder(orderId);
		
		// then : 이런 결과가 나온다.
		Order getOrder = orderRepository.find(orderId);
		
		em.flush();
		assertEquals("상품 주문시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 주문 수량만큼 다시  재고에서 늘어야 한다.", 10, item.getStockQuantity());
	}
	
	private Member createMember() {
		Member member = new Member();
		member.setName("Park Jong Hee");
		member.setAddress(new Address("충청북도 청주시", "상당구 산성로 55", "LH@ 104-1602"));
		em.persist(member);
		return member;
	}
	
	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}
	
}
