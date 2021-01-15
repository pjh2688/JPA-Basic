package com.bulkOperation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.bulkOperation.dto.Team;
import com.bulkOperation.dto.Teammate;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {
			Team team01 = new Team();
			team01.setName("Los Angeles Lakers");
			
			em.persist(team01);
			
			Team team02 = new Team();
			team02.setName("Los Angeles Clippers");
			
			em.persist(team02);
			
			Teammate teammate01 = new Teammate();
			teammate01.setName("Lebron James");
			teammate01.setAge(37);
			teammate01.setTeam(team01);
			
			em.persist(teammate01);
			
			Teammate teammate02 = new Teammate();
			teammate02.setName("Anthony Davis Jr.");
			teammate02.setAge(28);
			teammate02.setTeam(team01);
			
			em.persist(teammate02);
			
			Teammate teammate03 = new Teammate();
			teammate03.setName("Kawhi Leonard");
			teammate03.setAge(30);
			teammate03.setTeam(team02);
			
			em.persist(teammate03);
	
			em.flush();
			em.clear();
			
			// 쿼리가 날라갈 때나 커밋을 할 때는 flush 자동 호출(AUTO 모드일때) ->flush란 DB에 반영하는 것, clear란 영속성 컨텍스트를 비우는 것
			int resultCnt =  em.createQuery("update teammate tm set tm.age = 20")
						.executeUpdate();
			
			System.out.println("바뀐 엔티티 수 = " + resultCnt);
//			
//			// 1. 벌크 연산은 DB에 직접 쿼리를 날리기 때문에 영속성 컨텍스트에는 영향을 안준다. DB에만 반영한다는 말.
//			System.out.println("teammate01.getAge() = " + teammate01.getAge());  // 37
//			System.out.println("teammate02.getAge() = " + teammate02.getAge());  // 28
//			System.out.println("teammate03.getAge() = " + teammate03.getAge());  // 30
			
			
//			// 2. 그래서 영속성 컨텍스트를 em.clear()로 비워주고 다시 DB에서 가지고 오는 작업을 해줘야 한다.
			em.clear();
			
			// em.find하면 위에서 em.clear로 영속성 컨텍스트를 비워놨기 때문에 데이터베이스에서 데이터를 새로 꺼내온다.
			Teammate findTeammate = em.find(Teammate.class, teammate01.getId());
			
			System.out.println("findTeammate.getAge() = " + findTeammate.getAge());
			
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
 	* 벌크 연산[Bulk Operation] -> 특정 p.k나 값으로 데이터 1개를 수정하거나 삭제하는 경우를 제외한 모든 UPDATE, DELETE
 	 - 재고가 10개 미만인 모든 상품의 가격을 10% 인상시키려면?
 	 
 	 - JPA 변경 감지 기능(Dirty Checking)으로 실행하려면 너무 많은 SQL이 실행된다.
 	  (1) 재고가 10개 미만인 상품을 리스트로 조회하고
 	  (2) 상품 엔티티의 가격을 10% 증가 시키고
 	  (3) 트랜잭션 커밋 시점에 변경감지가 동작한다.
 	  
 	 - 변경해야할 데이터가 100건이라면 100번의 UPDATE SQL이 실행되어야 한다.
 	 
 	* 벌크 연산 예제
 	 - 쿼리 한 번으로 여러 테이블의 로우(행, 엔티티) 변경
 	 - executeUpdate()의 결과는 영향받은 엔티티의 수를 반환해준다.
 	 - UPDATE, DELETE 지원
 	 - INSERT(insert into .. select, 하이버네이트 지원)
 	 
 	* 벌크 연산 주의
     - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날린다.
      -> 잘못하면 꼬일 수도 있다는 의미.
      
    * 해결 방법 2가지
     (1) 벌크 연산을 먼저 실행
     (2) 벌크 연산을 수행 후 영속성 컨텍스트를 초기화시켜준다.(em.flush(), em.clear())
     
     
     // 벌크 연산해보고 데이터 원래대로 되돌려 놓는 작업
	int resultCnt =  em.createQuery("update teammate tm set tm.age = 37 where tm.name = 'Lebron James'")
			.executeUpdate();
			
	int resultCnt =  em.createQuery("update teammate tm set tm.age = 28 where tm.name = 'Anthony Davis Jr.'")
			.executeUpdate();
			
	int resultCnt =  em.createQuery("update teammate tm set tm.age = 30 where tm.name = 'Kawhi Leonard'")
		.executeUpdate();
		
		
	* 더티 체킹(Dirty Checking)이란?
	- JPA는 엔티티 매니저가 엔티티를 저장/조회/수정/삭제를 합니다.
	- 그런데 엔티티 매니저의 메서드를 찾아보면, 저장(persist)/조회(find)/수정(update)/삭제(delete)로 수정에 해당하는 메서드가 없습니다.
	- 대신에 수정(update)에 해당하는 더티 체킹(Dirty Checking)을 지원합니다.
	- 더티 체킹은 Transaction 안에서 엔티티의 변경이 일어나면, 변경 내용을 자동으로 데이터베이스에 반영하는 JPA 특징입니다.
 	 ->데이터베이스에 변경 데이터를 저장하는 시점
 	  1) Transaction commit 시점 
 	  2) EntityManager flush 시점 
 	  3) JPQL 사용 시점
 	 
	- 또한, 영속성 컨택스트(Persistence Context) 안에 있는 엔티티를 대상으로 더티 체킹이 일어납니다.
	- 여기서 Dirty란 "엔티티 데이터의 변경된 부분"으로 해석하시면 됩니다. 즉, 변경된 부분을 체크해서 DB에 반영한다는 뜻으로 해석합니다.
	
*/


