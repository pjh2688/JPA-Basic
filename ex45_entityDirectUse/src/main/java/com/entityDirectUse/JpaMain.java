package com.entityDirectUse;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.entityDirectUse.dto.Teammate;
import com.entityDirectUse.dto.Team;

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
			
			
			// 1. 엔티티를 파라미터로 전달 - 엔티티 전달
			// queryAPI, 파라미터 바인딩 - 이름 기준, 위치 기준
//			String query = "select tm from teammate tm where tm = :teammate";
			
//			List<Teammate> result = em.createQuery(query, Teammate.class)
//					.setParameter("teammate", teammate01)
//					.getResultList()
			
			// 2. 엔티티를 파라미터로 전달  - 식별자를 직접 전달
//			String query = "select tm from teammate tm where t.id = :teammate_id";
//			
//			List<Teammate> result = em.createQuery(query, Teammate.class)
//								.setParameter("teammate_id", teammate02.getId())
//								.getResultList();
//			
//			for(Teammate tm : result) {
//				System.out.println("name = " + tm.getName());
//				System.out.println("teamname = " + tm.getTeam().getName());
//			}
			
			
			Team team = em.find(Team.class, 1L);
			
			// 3. 외래 키 값 - 엔티티 전달
//			String query = "select tm from teammate tm where tm.team = :team";
//	
//			List<Teammate> result = em.createQuery(query, Teammate.class)
//					.setParameter("team", team)  // 엔티티를 넘겨도 @JoinColumn(name = "TEAM_ID")으로 인해 team_id랑 매핑된다.
//					.getResultList();

			// 4. 외래키 값 - 식별자를 직접 전달
			String query = "select tm from teammate tm where tm.team.id = :teamId";
			
			List<Teammate> result = em.createQuery(query, Teammate.class)
					.setParameter("teamId", team.getId())
					.getResultList();
			
			for (Teammate tm : result) {
				System.out.println("name = " + tm.getName());
				System.out.println("age" + tm.getAge());
			}	
			
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
 	* 엔티티 직접 사용
 	(1) 기본 키 값
 	 - JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용하게 된다.
 	 - [JPQL] 
 	  select count(m.id) from member m -> 엔티티의 아이디를 사용
 	  select count(m) from member m -> 엔티티를 직접 사용
 	  
 	 - [SQL] 
 	  select count(m.id) as cnt from member m
 	   
 	   -> 위 JPQL 두 개는 똑같은 SQL을 실행한다. 
 	   
 	 - 엔티티를 파라미터로 전달
 	  ex)
 	  	String query = "select t from teammate t where t = :teammate";
 	  	
 	 - 식별자를 직접 전달
 	 
 	(2) 외래 키 값
*/


