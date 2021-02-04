package com.jpaShop.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
@SequenceGenerator(name = "order_seq_generator", sequenceName = "order_seq", allocationSize = 50)
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 19. 생성 메소드를 이용한 생성만 가능하도록 생성자를 통한 생성 막아버리는 옵션.
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_generator")
	@Column(name = "ORDER_ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")  // 1. member와 order 사이에서 외래키를 member의 member_id로 설정한다는 의미. 다시 말하면 이쪽을 연관관계의 주인으로 설정한다는 의미.
	private Member member;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 6. OrderItem과 매핑. // 11. cascade = CascadeType.ALL -> persist 전파
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();  // 2. OrderItem 읽기 전용
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 8. 1:1 관계에선 연관 관계의 주인을 많이 Access하는 곳을 지정한다 그래서 여기서는 Order 엔티티로 지정. // 10. 일대일 -> fetch = FetchType.LAZY // 12. cascade = CascadeType.ALL -> persist 전파
	@JoinColumn(name = "DELIVERY_ID")  // 9. 연관관계 주인으로 정하기 위해  @JoinColumn을 붙여주고 외래키(DELIVERY_ID)를 설정해준다.
	private Delivery delivery;  // 3. 배송 정보.
	
	private LocalDateTime orderDate;  // 4. 주문 시간.
	
	@Enumerated(EnumType.STRING)  // 7. @Enumerated(EnumType.STRING)으로 ORDINAL(숫자로 표현)이 기본 
	private OrderStatus status;  // 5. 주문 상태[ORDER, CANCEL] -> ENUM 타입

	// 13. 연관 관계 편의 메소드 -> order랑 member 양방향
	public void addMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	
	// 14. 연관 관계 편의 메소드 -> order랑 orderItem 양방향
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	// 15. 연관 관계 편의 메소드 -> order랑 delivery 양방향
	public void addDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	
	// 16. 주문(Order) 생성 메서드 - 주믄이 들어왔을 때 이쪽으로
	// *매개변수에 ...오는 것에 대한 설명 :JDK1.5부터 매개변수의 개수를 동적으로 지정해 줄 수 있게 되었는데 이 기능을 가변인자(variable argument)라고 한다. 통칭 점점점 문법.
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		
		for(OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}
		
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		
		return order;
	}
	
	// 17. 비즈니스 로직
	
	/* 주문 취소  */
	public void cancel() {
		
		 // 이미 배송이 완료됬다면 
		 if(delivery.getStatus() == DeliveryStatus.COMP) {
			 throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
		 }
		 
		 // 위에 validation을 통과 했다면 주문 상태(OrderStatus)를 CANCEL로 바꿔준다.
		 this.setStatus(OrderStatus.CANCEL);
		 
		 // 주문(Order)에 담긴 orderItems들을 하나씩 모두 취소시켜준다. -> 주문 상품쪽 엔티티에다가도 cancel() 메소드 생성
		 for(OrderItem orderItem : this.orderItems) {
			 orderItem.cancel();  // 4. 주문상품에 있는 목록 아이템들을 수량만큼 복구시킨다.
		 }
	}
	
	// 18. 조회 로직
	
	/* 주문 상품 전체 가격 조회 */
	public int getTotalPrice() {
		int totalPrice = 0;
		
		for(OrderItem orderItem : orderItems) {
			totalPrice = orderItem.getTotalPrice();
		}
		
		return totalPrice;
	}
	
	/*
	 * 비즈니스 로직에 대한 설명
	  -> 화면과 DB 관련 로직을 빼면 남는 로직은 업무에 대한 처리 로직이다.
      -> DB에서 조회된 결과를 중간에 가공을 하거나 또른 다른데서 데이터를 가지고와서 이를 결합해서 보여주거나하는 로직이다.
	  -> 정리하면 화면과 DB를 제외한 나머지 로직들이 흔히 업무 중심적 로직이라고 하여 비즈니스 로직이라 불린다.
	 * */
	
}
