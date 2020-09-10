package com.inheritanceAssociativeMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.inheritanceAssociativeMapping.dto.Movie;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {
			
			Movie movie = new Movie();
			movie.setDirector("브래들리 쿠퍼");
			movie.setActor("레이디 가가");
			movie.setName("스타 이즈 본");
			movie.setPrice(10000);
			
			em.persist(movie);
			
			// 영속성 컨텍스트 비우기(안하면 영속성 컨텍스트에서 그냥 가지고 온다.)
			em.flush();
			em.clear();
			
			Movie findMovie = em.find(Movie.class, movie.getId());
			System.out.println("findMovie = " + findMovie);
			
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
 	*상속관계 매핑
 	 - 관계형 데이터베이스는 상속 관계가 없다.
 	 - 관계형 데이터베이스의 수퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사.
 	 - 상속관계 매핑 : 객체의 상속, 구조와 데이터베이스의 수퍼타입, 서브타입 관계를 매핑.
 	 - 수퍼타입, 서브타입 논리 모델을 실제 물리 모델로 구현하는 3가지 방법
 	  (1) 각각 테이블로 변환 -> 조인 전략.
 	  (2) 통합 테이블로 변환 -> 단일 테이블 전략.
 	  (3) 서브타입 테이블로 변환 -> 구현 클래스마다 테이블을 만들어 주는 전략.
 	  
 	*주연 어노테이션
 	1) @Inheritance(strategy=InheritanceType.XXX)
 	 - JOINED : 조인 전략
 	 - SINGLE_TABLE : 단일 테이블 전략
 	 - TABLE_PER_CLASS : 구현 클래스마다 테이블을 만들어주는 전략.
 	2) @DiscriminatorColumn(name="DTYPE") -> *discriminate : 식별하다.
 	3) @DiscriminatorValue("XXX")
 	
 	*조인 전략 -> 각각 테이블로 변환.
 	 -장점
 	  -> 테이블 정규화.
 	  -> 외래키 참조 무결성 제약조건 활용가능.
 	  -> 저장공간 효율화.
 	  
 	 -단점
 	  -> 조회시 조인을 많이 사용하여 성능 저하
 	  -> 조회 쿼리가 복잡하다.
 	  -> 데이터 저장시 INSERT SQL 2번 호출.
 	  
 	*단일 테이블 전략 
 	 -장점
 	  -> 조인이 필요 없으므로 일반적으로 조회 성능이 빠르다.
 	  -> 조회 쿼리가 단순하다.
 	  
 	 -단점
 	  -> 자식엔티티가 매핑한 컬럼은 모두 null값을 허용한다.
 	  -> 단일테이블에 모든 것을 저장하므로 테이블이 커질 수 있고 상황에 따라서 조회 성능이 오히려 느려질 수 있다.
 	  
 	*구현 클래스마다 테이블 전략 -> 이 전략은 데이터베이스 설계자와 ORM 전문가 둘다 추천하지 않는 전략, 쓰지 말자
 	 -장점
 	  -> 서브 타입을 명확하게 구분해서 처리할 때 효과적
 	  -> not null 제약조건 사용 가능.
 	 
 	 -단점
 	  -> 여러 자식 테이블을 함께 조회할 때 성능이 느리다(UNION SQL).
 	  -> 자식 테이블을 통합해서 쿼리를 날리기 어렵다.
*/


