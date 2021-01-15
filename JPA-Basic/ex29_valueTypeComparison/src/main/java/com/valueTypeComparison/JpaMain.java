package com.valueTypeComparison;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.valueTypeComparison.dto.Address;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			
			int a = 10;
			int b = 10;
			
			System.out.println("a == b : " + (a == b));
			
			Address address1 = new Address("충청남도 천안시 병천면", "충절로 1600", "31253");
			Address address2 = new Address("충청남도 천안시 병천면", "충절로 1600", "31253");
			
			System.out.println("address1 == address2 : " + (address1 == address2));
			System.out.println("adress1.equals(address2) : " + (address1.equals(address2)));
			
			tx.commit();  // 5. 정상적으로 오류없이 여기까지 왔다면 트랜잭션 커밋 .
			
		} catch(Exception e) {  // 6. 수행 도중 오류가 있다면 트랜잭션 롤백.
			tx.rollback();
			System.out.println("e = " + e);
			
		} finally {  // 7. 5번을 정상적으로 수행했다면 em 객체 close
			em.close();  
		}
		// 여기는 데이터 처리하는 곳(끝)
		emf.close();   // 8. try-catch문을 정상으로 빠져나왔따면 emf 객체 close
	}

}
/*
  *값 타입의 비교(임베디드타입일 경우)
  - 값 타입 : 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 보아야 함.
   ex) int a = 10;
       int b = 10;
      -> true 
       Address a = new Address("서울시");
       Address b = new Address("서울시");
      -> false
      
  - 동일성(identity) 비교 : 인스턴스의 참조 값을 비교, == 사용.
  - 동등성(equivalence) 비교 : 인스턴스의 값을 비교, equals(); 사용
  - 값 타입은 a.equals(b)를 사용해서 동등성 비교를 해야 한다.
  - 값 타입의 equals() 메소드를 적절하게 재정의해야 한다. -> 주로 모든 필드를 재정의해줘야 한다.
  - equals() 메소드는 기본적으로 == 비교이다. 그래서 override해줘야 한다.
*/    


