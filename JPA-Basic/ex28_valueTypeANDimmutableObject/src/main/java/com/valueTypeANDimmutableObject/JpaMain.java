package com.valueTypeANDimmutableObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.valueTypeANDimmutableObject.dto.Address;
import com.valueTypeANDimmutableObject.dto.Worker;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			
			Address address = new Address("청주시 상당구", "산성로 55", "LH@ 104-1602");
			
			Worker worker1 = new Worker();
			worker1.setName("박봉갑");
			worker1.setHomeAddress(address);
			em.persist(worker1);
			
			Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
			
			Worker worker2 = new Worker();
			worker2.setName("이태경");
			worker2.setHomeAddress(copyAddress);
			em.persist(worker2);

			
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
   *값 타입과 불변 객체 : 값 타입은 복잡한 객체 세상을 조금이라도 단순화하려고 만든 개념이다. 따라서 값 타입은 단순하고 안전하게 다룰 수 있으야 한다. 
	
   *값 타입 공유 참조
    - 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험하다.
    - 부작용(Side Effect) 발생.
    
   *객체 타입의 한계
    - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
    - 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입이다.
    - 자바 기본 타입에 값을 대입하면 값을 복사한다.
    - 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.
    - 객체의 공유 참조는 피할 수 없다.
    
   *불변 객체
    - 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단.
    - 값 타입은 불변 객체(immutable object)로 설계해야한다.
    - 불변 객체란 생성 시점 이후 절대 값을 변경할 수 없는 객체이다.
    - 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 된다.
    - 참고 Integer, String은 자바가 제공하는 대표적인 불변 객체.
    
   "불변이라는 작은 제약으로 부작용이라는 큰 재앙을 막을 수 있다."
*/


