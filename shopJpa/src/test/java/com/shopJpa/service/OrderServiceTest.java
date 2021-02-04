package com.shopJpa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.Member;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderStatus;
import com.shopJpa.domain.item.Book;
import com.shopJpa.exception.NotEnoughStockException;
import com.shopJpa.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception {
		// given
		Member member = new Member();
		
		member.setName("박종희");
		member.setAddress(new Address("청주시", "상당구 산성로", "55"));
		
		em.persist(member);
		
		Book book = new Book();
		book.setName("JPA ORM 표준");
		book.setPrice(10000);
		book.setStockQuantity(10);
		
		em.persist(book);
		
		// when
		Long orderId = orderService.order(member.getId(), book.getId(), 2);
		
		// then
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("주문한 상품 종류의 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
		assertEquals("주문 가격은 가격*수량이다.", 10000*2, getOrder.getTotalPrice());
		assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
	}
	
	@Test
	public void 주문취소() throws Exception {
		// given
		Member member = new Member();
		
		member.setName("박종희");
		member.setAddress(new Address("청주시", "상당구 산성로", "55"));
		
		em.persist(member);
		
		Book book = new Book();
		book.setName("JPA ORM 표준");
		book.setPrice(10000);
		book.setStockQuantity(10);
		
		em.persist(book);
		
		Long orderId = orderService.order(member.getId(), book.getId(), 2);
		
		// when
		orderService.cancelOrder(orderId);
		
		// then
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals("주문 취소시 상태는 CANCEL이다.", OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStockQuantity());
	}
	
	@Test(expected = NotEnoughStockException.class)
	public void 상품주문_재고수량초과() throws Exception {
		// given
		Member member = new Member();
		
		member.setName("박종희");
		member.setAddress(new Address("청주시", "상당구 산성로", "55"));
		
		em.persist(member);
		
		Book book = new Book();
		book.setName("JPA ORM 표준");
		book.setPrice(10000);
		book.setStockQuantity(10);
		
		em.persist(book);
		
		// when
		orderService.order(member.getId(), book.getId(), 11);
		
		// then
		fail("재고 수량 부족 예외가 발생해야 한다.");
	}
}
