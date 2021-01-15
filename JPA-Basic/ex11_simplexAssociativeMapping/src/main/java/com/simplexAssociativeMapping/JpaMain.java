package com.simplexAssociativeMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.simplexAssociativeMapping.dto.Team;
import com.simplexAssociativeMapping.dto.Teammate;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			// 1. 객체를 테이블에 맞춰서 모델링 할 경우(외래 키 식별자를 직접 다루는 경우)
			
			// team1 저장
			Team team1 = new Team();
			team1.setName("TeamA");
			em.persist(team1);
			
			// teammate1 저장
			Teammate teammate1 = new Teammate();
			teammate1.setUsername("member1");
//			teammate1.setTeamId(team1.getId()); //  객체 지향 모델링 X	
			teammate1.setTeam(team1); //  객체 지향 모델링 O	
			em.persist(teammate1);
			
			// team2 저장
			Team team2 = new Team();
			team2.setName("TeamB");
			em.persist(team2);

			// teammate 저장
			Teammate teammate2 = new Teammate();
			teammate2.setUsername("member2");
//			teammate2.setTeamId(team2.getId()); //  객체 지향 모델링 X	
			teammate2.setTeam(team2); //  객체 지향 모델링 O
			em.persist(teammate2);
			
			em.flush();  // 쿼리를 이 시점에 미리 날려버리고
			em.clear();  // 영속성 컨텍스트를 초기화(아무것도 없어지기 때문에 em.find를 하면 영속성 컨텍스트에 아무것도 없기 때문에 직접 데이터베이스에서 select 해온다.)
			
			Teammate findTeammate = em.find(Teammate.class, teammate2.getId());
			Team findTeam = findTeammate.getTeam();
			
			System.out.println("findTeam = " + findTeam.getName());
			
			// join 해보기 -> select * from teammate A join team t on A.TEAM_ID = t.TEAM_ID; 
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
 	*목표
 	 - 객체와 테이블 연관관계의 차이를 이해.
 	 - 객체의 참조와 테이블의 외래 키를 매핑.
 	
 	*용어
 	 - 방향(Direction) : 단방향, 양방향.
 	 - 다중성(Multiplicity) : 다대일(N:1), 일대다(1:N), 일대일(1:1), 다대다(N:M)
 	 - 연관관계의 주인(Owner) : 객체 양방향 연관관계는 관리
 	 
 	"객체지향 설계의 목표는 자율적인 객체들의 협력 공동체를 만드는 것이다." -> <객체지향의 사실과 오해> 저자 조영호.
 	
    <객체를 테이블에 맞추어 데이터 중심으로 모델링하면, 협력 관계를 만들 수 없다.>
     - 테이블은 <외래 키로 조인을 사용>해서 연관된 테이블을 찾는다.
     - 객체는 <참조를 사용>해서 연관된 객체를 찾는다.
     - 테이블과 객체 사이에는 이런 큰 간격이 있다. 
 	
 	
*/


