package com.duplexAssociativeMapping;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.duplexAssociativeMapping.dto.Team;
import com.duplexAssociativeMapping.dto.Teammate;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {
			
//			Teammate findTeammate = em.find(Teammate.class, 1L);  // 9. dto에서 p.k 기본키 id가 LONG 타입이므로 뒤에 L 붙여 줘야 한다.
//			List<Teammate> teammates = findTeammate.getTeam().getTeammates();  // 10. 양방향 연관 관계.
//			
//			for(Teammate teammate : teammates) {
//				System.out.println("teammate = " + teammate.getUsername());
//			}
			
			Team team = new Team();
			team.setName("TeamA");
			// team.getTeammates().add(teammate);  // DB에 보면 값이 NULL값이 들어감. -> 연관 관계의 주인(Owner)가 아니기 때문
			em.persist(team);
			
			// 양방향 매핑시 가장 많이 하는 실수(연관관계의 주인(Owner)에 값을 입력하지 않는 경우)
			Teammate teammate = new Teammate();
			teammate.setUsername("teammate1");
			teammate.setTeam(team);
			em.persist(teammate);
			
			// 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자.(team, teammate)
			team.getTeammates().add(teammate);  // 다른 방법은 dto쪽 setter에서 처리.
			
			// 양방향 매핑 사용시 lombok을 사용한다면 toString()은 제외시키자.
			
			Teammate findTeammate = em.find(Teammate.class, teammate.getId());  // 9. dto에서 p.k 기본키 id가 LONG 타입이므로 뒤에 L 붙여 줘야 한다.
			List<Teammate> teammates = findTeammate.getTeam().getTeammates();  // 10. 양방향 연관 관계.
			
			System.out.println("==============");
			for(Teammate t : teammates) {
				System.out.println("t = " + t);
			}
			System.out.println("==============");
			em.flush();
			em.clear();
			
					
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
 	*데이터 중심 설계의 문제점
 	- 현재 방식은 객체 설계를 테이블 설계에 맞춘 방식.
 	- 테이블의 외래키를 객체에 그대로 가져옴
 	- 객체 그래프 탐색이 불가능
 	- 참조가 없으므로 UML도 불가능 -> *UML[Unified Modeling Language] : 개발자간의 의사소통을 원활하게 하기 위해 표준화한 모델링 언어.
 	
 	*양방향 매핑 정리
 	- 단방향 매핑만으로도 이미 연관관계 매핑은 완료된 것(JPA설계시 단방향 매핑으로 설계를 끝내야 된다.).
 	- 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추가된 것 뿐.
 	- JPQL에서 역방향으로 탐색할 일이 많음.
 	- 단방향 매핑을 우선 잘 해놓고 양방향 매핑은 필요할 때 추가해서 써도된다.(테이블에 영향을 주지 않음) -> 단방향 매핑으로 설계를 끝낸 후 추후에 필요할 때 양방향 매핑 추가를 고려해도 늦지 않다는 말. 

	*연관관계의 주인을 정하는 기준
	- 비즈니스 로직을 기준으로 연관관계의 주인을 선ㅌ낵하면 안된다.
	- 연관관계의 주인은 외래 키의 위치를 기준으로 정해야한다.
*/


