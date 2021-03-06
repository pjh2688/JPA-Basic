package com.jpaShop02;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.jpaShop02.dto.Order;
import com.jpaShop02.dto.OrderItem;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {
			
			// 양방향 설정 안하고 하드 코딩 방식
//			Order order = new Order();
//			em.persist(order);
//			
//			OrderItem orderitem = new OrderItem();
//			orderitem.setOrder(order);
//			em.persist(orderitem);
//			
			// 메소드를 삽입하여 양방향 설정 방식 
			Order order = new Order();
			order.addOrderItem(new OrderItem());
			

			tx.commit();  // 5. 정상적으로 오류없이 여기까지 왔다면 트랜잭션 커밋 .
			
		} catch(Exception e) {  // 6. 수행 도중 오류가 있다면 트랜잭션 롤백.
			tx.rollback();  
		} finally {  // 7. 5번을 정상적으로 수행했다면 em 객체 close
			em.close();  
		}
		// 여기는 데이터 처리하는 곳(끝)
		emf.close();   // 8. try-catch문을 정상으로 빠져나왔따면 emf 객체 close
	}

}
/*
 	*데이터 중심 설계의 문제점
 	- 현재 방식은 객체 설계를 테이블 설계에 맞춘 방식.
 	- 테이블의 외래키를 객체에 그대로 가져옴
 	- 객체 그래프 탐색이 불가능
 	- 참조가 없으므로 UML도 불가능 -> *UML[Unified Modeling Language] : 개발자간의 의사소통을 원활하게 하기 위해 표준화한 모델링 언어.

*/


