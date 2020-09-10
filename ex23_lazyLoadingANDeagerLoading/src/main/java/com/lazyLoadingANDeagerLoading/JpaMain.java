package com.lazyLoadingANDeagerLoading;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.lazyLoadingANDeagerLoading.dto.Team;
import com.lazyLoadingANDeagerLoading.dto.Teammate;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			
			// 프록시로 먼저 조회하면 프록시 객체가 남아잇어서 em.find로 다시 조회해도 프록시 객체가 반환되고
			// em.find를 먼저 조회하면 그 다음에 em.getReference로 조회를해도 프록시 객체가 아닌 실제 객체가 반환된다는 게 핵심.

			Team teamA = new Team();
			teamA.setName("LA레이커스");
			em.persist(teamA);
			
			Team teamB = new Team();
			teamB.setName("LA클리퍼스");
			em.persist(teamB);
			
			Teammate teammate1 = new Teammate();
			teammate1.setUsername("박종희");
			teammate1.setTeam(teamA);
			em.persist(teammate1);
			
			Teammate teammate2 = new Teammate();
			teammate2.setUsername("이태경");
			teammate2.setTeam(teamB);
			em.persist(teammate2);
			
			em.flush();
			em.clear();
			
			// 지연 로딩 LAZY를 사용해서 프록시(가짜 객체)로 조회.
//			Teammate tm = em.find(Teammate.class, teammate1.getId());
//			System.out.println("teammate1 = " + tm.getTeam().getClass());  // 프록시 객체를 가져온다.
			
//			System.out.println("============================");
//			tm.getTeam().getName();  // Teammate 객체 tm이 실제로 사용이 될 때 쿼리가 나간다(초기화).
//			System.out.println("============================");
			
			// 프록시와 즉시로딩시 주의 예시 
//		    List<Teammate> teammates = em.createQuery("select tm from teammate tm", Teammate.class).getResultList();  // jpql에선 dto에 클래스의 @Entity 어노테이션 name 속성의 값을 적어줘야한다.(대소문자 구분한다.) -> 테이블이 아니라 엔티티를 대상으로 하기 때문이다. 내 프로젝트에서는 DB에는 Teammate 이름으로, dto는  @Entity name이 teammate로 되어 있다.
			
			// fetch join 맛보기
			List<Teammate> teammates = em.createQuery("select tm from teammate tm join fetch tm.team", Teammate.class).getResultList();
		    // SQL : select * from Teammate
		    // SQL : select * from Teammate where team.id = teammate.id;
		    
			teammates.size();  // 경고창 삭제 용(아무 의미없음)
			
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
 	* 주의 : 초기 설정시 pom.xml에서 version 낮추고 db랑 하이버네이트 의존성 넣어주고 maven update 한 후에는 다시 maven update를 해버리면 오류난다. 다시 하지 말란 얘기.
 	
 	* 프록시와 즉시로딩 주의 사항
 	 - 가급적이면 지연 로딩만 사용하자.(특히 실무에서)
 	 - 즉시 로딩을 사용하다 보면 예상하지 못한 SQL이 발생하기도 한다.
 	 - 즉시 로딩은 JPQL(Java Persistence Query Language)에서 N+1 문제를 일으킨다.
 	 - @ManyToOne, @OneToOne은 기본이 즉시 로딩(EAGER)이기 때문에 지연 로딩(LAZY)로 설정해줘야 한다.
 	 - @OneToMany, @ManyToMany는 기본이 지연로딩(LAZY)이다.
 	 
 	*JPQL(Java Persistence Query Language)
	  - JPA를 사용하면 엔티티 객체를 중심으로 개발.
	  - 문제는 검색 쿼리.
	  - 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색.
	  - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능.
	  - 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요.
	  - JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공.
	  - SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원.
	  - JPQL은 엔티티 객체를 대상으로 쿼리.
	  - SQL은 데이터베이스 테이블을 대상으로 쿼리.
	  - 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리이다.
	  - SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.(JPQL로 명령하면 내부에서 DB에 맞게 변환해준다는 말)
	  - JPQL을 한마디로 정의하면 객체 지향 Structure Query Language.
	  
	*지연 로딩 활용 - 이론
	  - Teammate와 Team은 자주 함께 사용한다면 -> 즉시 로딩
	  - Teammate와 Order는 가끔 사용한다면 -> 지연 로딩
	  - Order와 Product는 자주 함께 사용한다면 -> 즉시 로딩
	 -> 이론적으론 이렇지만 실무에선 그냥 다 지연로딩으로 써라.
	
	*지연 로딩 활용 - 실무
	  - 모든 연관관계에 지연 로딩을 사용하도록 권장.
	  - 실무에서는 즉시 로딩을 사용하지 말도록 권장.
	  - JPQL fetch 조인이나 엔티티 그래프 기능을 사용해라.(이건 뒤에서 설명)
	  - 즉시 로딩은 예상치 못한 쿼리가 나갈 수 있다. -> 실무에선 쓰지 말라는 말이다. 
	 
*/


