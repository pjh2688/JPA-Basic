package com.fetchJoin02;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.fetchJoin02.dto.Team;
import com.fetchJoin02.dto.Teammate;

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
			
			// 1. 페치 조인에는 원칙적으로는 별칭(alias)을 줄 수 없다.(하이버네이트에서는 가능은 하지만 쓰지 말자.) 
			// -> 별칭을 준다음 where을 달아서 조건을 줘 특정 몇개의 데이터만 불러오게 해버리면 문제가 생길 수 있다.
			// -> mappedby 된 컬렉션 객체는 원래 데이터를 모두 조회해오는 것으로  설계되어 있기 때문
//			String query = "select t from team t join fetch t.teammates tm";
			
			// 2. 둘 이상의 컬렉션은 페치 조인 할 수 없다. -> 페치 조인 사용시 컬렉션 객체는 딱 1개만 지정할 수 있다.
			// -> 현재 team 객체안에 컬렉션(List) 객체가 1개만 있지만 만약 컬렉션 객체가 하나 더 있다면 페치 조인하지 말자.
//			String query = "select t from team t join fetch t.teammates";
			
//			List<Team> result = em.createQuery(query, Team.class).getResultList();
//			
//			System.out.println("result = " + result.size());  // join fetch 사용 안하면 2개, join fetch 사용하면 3개(데이터가 뻥튀기 되었기 때문), 실제 팀은 LA레이커스랑 LA클리퍼스 2개만 들어가 있는데 join fetch를 사용하면서 teammate쪽의 데이터랑 섞여버려서 그런 듯(일대다 관계에서만, 다대일은 아님)
//			
//			for(Team team : result) {
//				System.out.println("team = " + team.getName());
//				System.out.println("teammates = " + team.getTeammates().size());
//				
//				for(Teammate teammate : team.getTeammates()) {
//					System.out.println("teammate = " + teammate);
//					 
//				}
//			}
			
			// 3. 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 업다.
			// -> WARN org.hibernate.hql.internal.ast.QueryTranslatorImpl - HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
//			String query = "select t from team t join fetch t.teammates tm";
			
//			List<Team> result = em.createQuery(query, Team.class)
//					.setFirstResult(0)
//					.setMaxResults(1)
//					.getResultList();
//	
//			System.out.println("result = " + result.size());
//	
//			for(Team team : result) {
//				System.out.println("team = " + team.getName());
//				System.out.println("teammates = " + team.getTeammates().size());
//		
//				for(Teammate teammate : team.getTeammates()) {
//					System.out.println("teammate = " + teammate);
//			 
//				}
//			}
			
			
			// 4. 3번  해결 방법 1 -> 뒤집는다.(일대다가 다대일로 바뀌기 때문)
//			String query = "select tm from teammate tm join fetch tm.team t";
//			
//			List<Teammate> result = em.createQuery(query, Teammate.class)
//							.setFirstResult(0)
//							.setMaxResults(1)
//							.getResultList();
//			
//			System.out.println("result = " + result.size());
//			
//			for(Teammate teammate : result) {
//				System.out.println("teammate = " + teammate.getName());
//				System.out.println("team = " + teammate.getTeam().getName());
//				
//				for(Teammate teammates : teammate.getTeam().getTeammates()) {
//					System.out.println("teammates = " + teammates.getName());
//					 
//				}
//			}
			
			// 5. 3번  해결 방법 2(페이징 쿼리가 나가나 지연로딩으로 설정되어 있어서 한번에 결과가 안나옴) -> 쿼리가 3번 나가니 성능이 안나온다.
			// -> select 하려는 team 엔티티 쪽 컬렉션에다가 @BatchSize 어노테이션을 붙여주고 size를 1000이하의 수로 잡아준다. 그러면 쿼리가 더 적게 나간다.
			// -> 또는 영속성 컨텍스트(persistence.xml)에  <property name="hibernate.default_batch_fetch_size" value="100"/>을 추가해준다.
			String query = "select t from team t";
			
			List<Team> result = em.createQuery(query, Team.class)
							.setFirstResult(0)
							.setMaxResults(2)
							.getResultList();
			
			System.out.println("result = " + result.size());
			
			for (Team team : result) {
				System.out.println("team = " + team.getName());
				System.out.println("teammates = " + team.getTeammates().size());

				for (Teammate teammate : team.getTeammates()) {
					System.out.println("teammate = " + teammate.getName());

				}
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
 	* 페치 조인의 특징과 한계
 	 (1) 페치 조인 대상에는 별칭을 줄 수 없다.
 	  - 하이버네이트를 쓰면 가능하지만 가급적 사용하지 말자.
 	 
 	 (2) 둘 이상의 컬렉션은 페치 조인 할 수 없다.
 	 
 	 (3) 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 업다.
 	  - 그러나 예외적으로 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인을 하더라도 페이징 API를 사용할 수 있다.
 	  - 하이버네이트는 경고 로그를 남기고 메모리에서 페이징을 하기 때문에 페이징이 되더라도 위험하다.
 	  
 	 (4) 연관된 엔티티들을 SQL 한 번으로 조회할 수 있다. -> 성능 최적화.
 	 
 	 (5) 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선한다.
 	  ex) @OneToMany(fetch = FetchType.LAZY)  // 글로벌 로딩 전략
 	   -> 이 전략과 관계 없이 페치 조인을 우선시 한다는 말.
 	  
 	 (6) 실무에서 글로벌 로딩 전략은 모두 지연 로딩으로 한다.
 	 
 	 (7) 최적화가 필요한 곳은 페치 조인 적용.
 	 
 	* 페치 조인 정리
 	 - 모든 것을 페치 조인으로 해결할 수는 없다.
 	 - 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적이다.
 	 - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 다른 결과를 내야 한다면, 페치 조인보다는 일반 조인을 사용하는 것을
 	  권장하고 필요한 데이터들만 조회해서 DTO(Data Transfer Object, 데이터 전송 객체)로 반환하는 것이 효과적이다.
*/


