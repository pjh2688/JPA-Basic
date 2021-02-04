package com.shopJpa.domain;

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
import javax.persistence.Table;

// import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")  // order by 때문에 테이블명을 orders로
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자로 직접 생성하지말고 생성 메소드로만 생성하게 제약한다.
public class Order {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")  // 외래키를 Member엔티티의 member_id로 Order테이블에 생성, 연관 관계 주인을 외래키가 있는 이곳을 지정.
	private Member member;  //	private Member member = new ByteBuddyInterceptor(); -> 생략되어 있음.
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  // 1:N은 기본(default) Lazy
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // OneToOne은 접근을 많이 하는 쪽에 FK를 설정한다.
	@JoinColumn(name = "delivery_id")  // FK를 delivery_id로 설정.
	private Delivery delivery;
	
	private LocalDateTime orderDate;  // 주문 시간
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;  // 주문 상태[ORDER, CANCEL]
	
	// 양방향일때 사용 //
	
	/* 연관 관계 편의 메소드(N:1) -> N:1은 set */
	public void setMember(Member member) {
		// 주문(Order)이 들어오면 member를 세팅한다.
		this.member = member;
		
		// member를 세팅해주는 동시에 양방향으로 member 엔티티에 있는 order에 여기에 있는 Order를 세팅해준다.(양방향)
		member.getOrders().add(this);
	}
	
	/* 연관 관계 편의 메소드(1:N) -> 1:N은 add */
	public void addOrderItem(OrderItem orderItem) {
		// 주문이 들어오면 Order에 있는 orderItems에 먼저 add(추가)를 해주고
		orderItems.add(orderItem);
		
		// 반대편 orderItem쪽에는 여기서 생긴 주문(order)를 set해준다.
		orderItem.setOrder(this);
	}
	
	/* 연관 관계 편의 메소드(1:1) -> 1:1은 set */
	public void setDelivery(Delivery delivery) {
		// 주문이 들어오면 order에 들어온 주문 엔티티를 세팅해준다.
		this.delivery = delivery;
		
		// 반대로 위에서 세팅된 order 엔티티를 delivery쪽 order에 세팅해준다.
		delivery.setOrder(this);
	}
	
	/* static 생성 메소드 : 주문과 같은 경우는 별도의 생성 메소드가 있으면 좋다(여러 엔티티랑 연관관계로 맺어졌기 떄문) */
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {  // 가변인자 (variable argument) ... : 일정한 형의 변수를 여러 개 전달해야 할 때
		Order order = new Order();
		
		order.setMember(member);
		order.setDelivery(delivery);
		
		for(OrderItem orderItem : orderItems) {  // 주문 상품은 여러 개일 수도 있기 떄문에 이렇게 처리한다.
			order.addOrderItem(orderItem);
		}
		
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		
		return order;
	}
	
	/* 비즈니스 로직 */
	/**
	 *  주문 취소
	 */
	public void cancel() {
		
		if(delivery.getStatus() == DeliveryStatus.COMP) {  // 이미 배송이 완료되 었으면 예외를 터트린다.
			throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능합니다.");
		}
		
		this.setStatus(OrderStatus.CANCEL);
		
		for(OrderItem orderItem : this.orderItems) {
			orderItem.cancel();
			
		}
	}
	
	/* 조회 로직 */
	/**
	 *  전체 주문 가격 조회
	 */
	public int getTotalPrice() {
		
		int totalPrice = 0;
		
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
		
		return totalPrice;
	}
}
