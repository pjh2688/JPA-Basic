package com.shopJpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopJpa.domain.item.Item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@JsonIgnore  // 양방향 연관관계에서는 한쪽을 @JsonIgnore 꼭 달아줘야 한다.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	
	private int orderPrice;  // 주문 가격
	private int count;  // 주문 수량

/*  
 * @NoArgsConstructor(access = AccessLevel.PROTECTED)랑 같은 의미임. -> lombok에서 제공
	protected OrderItem() {  // 생성자를 쓰지말고 생성메서드를 통해서만 생성하게 생성자를 protected로 막아둔다.
		// TODO Auto-generated constructor stub
	}
*/
	/* static 생성 메소드 */
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);
		
		// 주문을 하면 기본적으로 item에서 재고를 주문 수량만큼 감소 시켜줘야 한다.
		item.removeStock(count);
		return orderItem;
	}
	
	/* 비즈니스 로직 */
	/**
	 *  주문 취소
	 */
	public void cancel() {
		getItem().addStock(count);  // 주문 취소가 되면  OrderItem에서는 getter로 item을 가져온 뒤 주문 수량(count)을 다시 채워넣는다.
	}
	
	/* 조회 로직 */
	/**
	 *  주문 상품들의 총 가격(주문 가격 X 주문 수량)
	 */
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
	
}
