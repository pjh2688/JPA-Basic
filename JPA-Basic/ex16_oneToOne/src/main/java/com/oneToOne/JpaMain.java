package com.oneToOne;

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
 	*일대일 관계
 	- 일대일 관계는 그 반대도 일대일이다.
 	- 주 테이블이나 대상 테이블 중에 외래 키 선택 가능(주 테이블에 외래 키 or 대상 테이블에 외래 키).
 	- 사용할 외래 키에 데이터베이스 유니크(UNI) 제약조건 추가.
 	
 	*일대일 관계에서 대상 테이블에 외래 키 단방향 정리
 	- 일대일 관계에서는 주 테이블을 이미 설정해서 외래키를 걸어 놨다가 양방향을 위해 대상 테이블에도 외래키를 줘버렸는데 대상 테이블에서 단방향 관계를 쓰기 위해
 	  주 테이블의 외래키를 지워버린 상태로 납두면 그런 단방향 관계는 JPA에서 지원하지 않는다.
 	- 일대일 관계 양방향 관계는 지원한다.
 	
 	* 일대일 관계 정리
 	- 주 테이블을 지정하고 그 곳에 외래 키 -> 주 테이블이란 자주 사용하는 테이블을 지칭.
 	 -> 주 객체가 대상 객체의 참조를 가지는 것처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾는다.
 	 -> 객체 지향 개발자가 선호하는 방식이다.
 	 -> JPA 매핑이 편리하다.
 	 -> 장점 : 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 없는지 확인할 수 있다.
 	 -> 단점 : 값이 없으면 외래 키에 null값을 허용한다.
 	 
 	- 주 테이블에 걸어놨던 외래키를 대상 테이블에다가 걸어줬을 때 
 	 -> 대상 테이블에 외래 키가 존재.
 	 -> 전통적인 데이터베이스 개발자가 선호하는 방식이다.
 	 -> 장점 : 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때에도 테이블 구조를 유지할 수 있다.
 	 -> 단점 : 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩된다.(프록시는 뒤에서 설명)
 	 
*/


