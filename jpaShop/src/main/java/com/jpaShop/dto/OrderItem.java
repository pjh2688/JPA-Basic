package com.jpaShop.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity
@SequenceGenerator(name = "orderitem_seq_generator", sequenceName = "orderitem_seq", allocationSize = 50)
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 13. 생성 메소드를 이용한 생성만 가능하도록 생성자를 통한 생성 막아버리는 옵션.
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderitem_seq_generator")
	@Column(name = "ORDER_ITEM_ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)  // 6. Item Entity와 매핑  // 8. 다대일 -> fetch = FetchType.LAZY
	@JoinColumn(name = "ITEM_ID")  // 7. 외래키를 ITEM 엔티티의  ITEM_ID로 사용.
	private Item item;  // 5. 연관 관계 매핑 설정
	
	@ManyToOne(fetch = FetchType.LAZY) // 1. Order Entity와 매핑  // 9. 다대일 -> fetch = FetchType.LAZY
	@JoinColumn(name = "ORDER_ID")  // 2. @JoinColumn으로 연관 관계의 주인을 설정.
	private Order order; 
	
	private int orderPrice;  // 3. 주문(당시) 가격
	
	private int count;  // 4. 주문(당시) 수량
	
	// 5. 비즈니스 로직
	public void cancel() { // 6. 주문 취소
		this.getItem().increaseStock(count);  // 7. 6번이 실행되면 상품에서 해당 수량만큼 취소된 물품을 다시 채워 넣어야(increase) 된다.  
	}
	
	// 8. 조회 로직(전체 주문상품의 가격 조회)
	public int getTotalPrice() {
		return this.getOrderPrice() * this.getCount();  // 9. 주문 가격 X 주문 수량
	}
	
	// 10. 주문상품(OrderItem) 생성 메서드
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		// 11. 주문 상품을 생성해주고
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);
		
		// 12. 주문 상품의 수량만큼 재고에서 decrease 시켜준다.
		item.decreaseStock(count);
		
		return orderItem;
	}
}
