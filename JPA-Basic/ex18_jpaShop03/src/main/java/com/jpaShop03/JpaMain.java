package com.jpaShop03;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			
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
 	*다대다 관계  
 	 - 관계형 데이터베이스는 정규화된 테이블 2개만으로 다대다 관계를 표현할 수 없다.
 	 - 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야한다.
 	 - 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 표현이 가능하다.
 	 - @ManyToMany 사용.
 	 - @JoinTable로 연결 테이블 지정.
 	 - 다대다 매핑은 단방향, 양방향 모두 가능.
 	 - 연결 테이블이 단순히 연결만 하고 끝나지 않는다.
 	 - 생성되는 중간테이블에는 양족 테이블을 매핑할 수 있는 매핑 정보만 들어가고 다른 정보(주문 시간, 주문 수량 등)는 들어갈 수 없기 때문에 편리해 보이지만 실무에서 사용하진 않는다.
 	 
 	*다대다 관계의 한계 극복 방법
 	 - @ManyToMany를 -> @OneToMany, @ManyToOne 다대일, 일대다로 바꾸고
 	 - 연결 테이블용 엔티티를 직접 추가한다.(자동 생성되는 중간 테이블을 엔티티로 승격시킨다.)
 	
 	*N:M 관계는 1:N, N:1로 바꿔라.
 	 - 테이블의 N:M 관계는 중간 테이블을 이용해서 1:N, N:1로 나눠서 따로 매핑해준다.
 	 - 실전에서는 중간 테이블이 단순하지 않다.
 	 - @ManyToMany 사용시 제약 2가지 : 필드 추가가 안됨, 객체(Entity)와 테이블 사이의 불일치
 	 
 	*@JoinColumn : 외래 키를 매핑할 때 사용
 	 - 속성
 	  (1) name : 매핑할 외래 키 이름(기본값 : 필드명 + _ + 참조하는 테이블의 기본 키 컬럼명)
 	  (2) referencedColumnName : 외래 키가 참조하는 대상 테이블의 컬럼명(기본값 : 참조하는 테이블의 기본 키 컬럼명)
 	  (3) foreignKey(DDL) : 외래 키 제약조건을 직접 지정할 수 있다.(이 속성은 테이블을 생성할 때만 사용한다)
 	  (4) unique, nullable, insertable, updatable, columnDefinition, table -> @Column 속성과 같음
*/


