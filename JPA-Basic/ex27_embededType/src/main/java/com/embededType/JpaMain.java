package com.embededType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.embededType.dto.Address;
import com.embededType.dto.Period;
import com.embededType.dto.Worker;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			
			Worker worker01 = new Worker();
			worker01.setName("박종희");
			worker01.setHomeAddress(new Address("충청북도 청주시", "상당구 산성로", "55"));
			worker01.setWorkPeriod(new Period());
			
			em.persist(worker01);
			
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
     *임베디드 타입[Embedded Type]
      - 새로운 값 타입을 직접 정의할 수 있다.
      - JPA는 임베디드 타입(embedded type)이라고도 한다.
      - 주로 기본 값 타입을 모아서 만들기 때문에 복합 값 타입이라고도 한다.
      - int, String과 같은 값 타입.
      
     *임베디드 타입의 예시 
      (1) 회원 엔티티는 번호(id), 이름(name), 근무 시작일(startDate), 근무 종료일(endDate), 주소 도시(city), 주소 번지(street), 
            주소 우편번호(zipcode)를 가진다.
            
      (2) (1)을 실제로 사용할 땐 주로 회원 엔티티는 번호(id), 이름(name), 근무 기간(workPeriod), 집 주소(homeAddress)를 가진다라고 추상화시킨다.
    
      (3) (1) -> (2) 으로 추상화 시킬 때 엔티티들을 묶어주는 것이 임베디드 타입이다.
     
     *임베디드 타입 사용법
      - @Embeddable : 값 타입을 정의하는 곳에 표시.
      - @Embedded : 값 타입을 사용하는 곳에 표시.
      - 기본 생성자 필수.
      
     *임베디드 타입의 장점
      - 재사용이 가능하다.
      - 높은 응집도를 보여준다.
      - Period.isWork()처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있다.
      - 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존한다.
      
     *임베디드 타입과 테이블 매핑
      - 임베디드 타입은 엔티티의 값일 뿐이다.
      - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
      - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능하다.
      - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많다.
      
     *@AttributeOverride : 속성 재정의
      - 한 엔티티에서 같은 값 타입을 사용할 때 사용.
      - 컬럼 명이 중복됨
      - 여러 개 일 땐 @AttrivuteOverrides, 하나 일 땐 @AttributeOverride를 사용해서 컬럼 명 속성을 재정의.
      
     *임베디드 타입과 null
      - 임베디드 타입의 값이 null이면 매핑한 컬럼 값은 모두 null값이 된다.
*/


