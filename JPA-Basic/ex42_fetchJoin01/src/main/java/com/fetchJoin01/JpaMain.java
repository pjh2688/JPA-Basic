package com.fetchJoin01;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.fetchJoin01.dto.Team;
import com.fetchJoin01.dto.Teammate;

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
		
			// 1. 엔티티 페치 조인(처음에 ManyToOne은 지연로딩으로 설정을 해놓기 때문에 프록시를 사용한다.)
//			String query = "select tm from teammate tm";
//			String query = "select tm from teammate tm join fetch tm.team";  // 지연 로딩을 설정해 놓아도 join fetch를 사용하면 fetch join을 우선으로 실행된다.
			
//			List<Teammate> result = em.createQuery(query, Teammate.class).getResultList();
			
//			for(Teammate teammates : result) {
//				System.out.println("선수 정보 : " + teammates.getName() + ", " + teammates.getTeam().getName());
			//  회원 1, 팀 Los Angeles Lakers(SQL) -> 데이터베이스에 쿼리를 날려서 1차캐시(영속성컨텍스트)에 데이터를 올려놓는다.
			//  회원 2, 팀 Los Angeles Lakers(1차캐시) -> 영속성 컨텍스트에 이미 팀 Los Angeles Lakers라는 데이터가 올라가 있기 때문에 프록시(가짜 객체)로 조회한다.
			//  회원 3, 팀 Los Angeles Clippers(SQL) -> 팀B가 없기 때문에 데이터베이스에 쿼리를 다시 날려서 1차캐시(영속성컨텍스트)에 데이터를 올려놓는다.
			
			//  회원 100명 -> N + 1 => 쿼리가 100번 나감, 해결할려면 fetch join으로 해결해야한다.
			
			//  String query = "select tm from teammate tm join fetch tm.team"; -> 이때에는 프록시 객체가아니라 실제 데이터이다. join fetch를 사용하면 처음에 데이터베이스에서 모두 가지고 온다
				
//			}
			
			// 2. 컬렉션 페치 조인
//			String query = "select t from team t join fetch t.teammates";
//			
//			List<Team> result = em.createQuery(query, Team.class).getResultList();
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
			
			// 3. JPA fetch join distinct -> 보통 SQL에서는 distinct로 걸러질라면 데이터가 완전 똑같아야 된다. 하나라도 다르면 안걸러진다. 그렇지만 JPA에서는 컬렉션에서 DISTINCT만 붙여주면 중복을 없애준다.
//			String query = "select distinct t from team t join fetch t.teammates";
//			
//			List<Team> result = em.createQuery(query, Team.class).getResultList();
			
		//	System.out.println("result = " + result.size());  // join fetch 사용 안하면 2개, join fetch 사용하면 3개(데이터가 뻥튀기 되었기 때문), 실제 팀은 LA레이커스랑 LA클리퍼스 2개만 들어가 있는데 join fetch를 사용하면서 teammate쪽의 데이터랑 섞여버려서 그런 듯(일대다 관계에서만, 다대일은 아님)
			
//			for(Team team : result) {
//				System.out.println("team = " + team.getName());
//				System.out.println("teammates = " + team.getTeammates().size());
//				
//				for(Teammate teammate : team.getTeammates()) {
//					System.out.println("teammate = " + teammate);
//					 
//				}
//			}
			
			// 4. fetch join이 아닌 일반 join
			String query = "select t from team t join t.teammates tm";
			
			List<Team> result = em.createQuery(query, Team.class).getResultList();
			
			System.out.println("result = " + result.size());  // join fetch 사용 안하면 2개, join fetch 사용하면 3개(데이터가 뻥튀기 되었기 때문), 실제 팀은 LA레이커스랑 LA클리퍼스 2개만 들어가 있는데 join fetch를 사용하면서 teammate쪽의 데이터랑 섞여버려서 그런 듯(일대다 관계에서만, 다대일은 아님)
			
			for(Team team : result) {
				System.out.println("team = " + team.getName());
				System.out.println("teammates = " + team.getTeammates().size());
				
				for(Teammate teammate : team.getTeammates()) {
					System.out.println("teammate = " + teammate);
					 
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
 	* JPQL - 페치 조인(fetch join) -> 실무에서 정말 중요하다.
 	 - SQL 조인의 한 종류가 아니다.
 	 - JPQL에서 성능 최적화를 위해 제공하는 기능이다.
 	 - 연관된 엔티티(Entity)나 컬렉션(Collection)을 SQL 한 번에 함께 조회하는 기능이다.
 	 - join fetch 명령어를 사용한다.
 	 - 페치조인 ::= [ LEFT [OUTER] | [INNER] ] JOIN FETCH 조인경로
 	 
 	* 엔티티 페치 조인(Entity Fetch Join)
 	 - 회원을 조회하면서 연관된 팀도 함께 조회(SQL 한 번에)
 	 - SQL을 보면 회원 뿐만 아니라 부서(D.*)도 함께 SELECT
 	 - [JPQL] -> select c from colleague c join fetch c.division
 	 - [SQL] -> select c.*, d.* from colleague c inner join division d on c.division.id = d.id

	 - 실행 시 쿼리 순서(지연로딩이기 때문)
		(1) 처음 실행 시(query)
	 	 -> select tm from teammate tm -> 처음에 teammate 3개를 가지고 온다.(처음에는 영속성 컨텍스트에 없으니 데이터베이스에서 직접)
		
		select
            teammate0_.TEAMMATE_ID as teammate_id1_1_,
            teammate0_.TEAMMATE_AGE as teammate_age2_1_,
            teammate0_.TEAMMATE_NAME as teammate_name3_1_,
            teammate0_.TEAM_ID as team_id4_1_ 
        from
            teammate teammate0_
     
     
    	(2) 반복문 루프 돌 때 -> 1,2는 레이커스팀
     	 -> System.out.println("선수 정보 : " + teammates.getName() + ", " + teammates.getTeam().getName());
      
        select
        	team0_.TEAM_ID as team_id1_0_0_,
        	team0_.TEAM_NAME as team_name2_0_0_ 
    	from
        	team team0_ 
    	where
        	team0_.TEAM_ID=?
       
       => 반복문에서 두번째로 데이터를 조회할 때는 영속성 컨텍스트에 이미 데이터가 있기 때문에 프록시(가짜 객체)를 조회한다. 쿼리가 첫번째 데이터할때만 나가고 두번재 데이터는 그대로 출력.
       => 이 구간에서  teammates.getTeam().getName() 연관 관계 매핑이된 team 이름을 불러 오기 때문에 이 시점에 영속성 컨텍스트에 팀명(LA레이커스)라는 데이터가 들어간다. 이거 실행하기 전에는 팀은 설정이 안되어 있음 


		(3) 3번째 클리퍼스팀을 조회할땐 클리퍼스란 팀이 영속성 컨텍스트에 없기 때문에 쿼리가 다시 날라간다.
	  	 ->
		select
        	team0_.TEAM_ID as team_id1_0_0_,
        	team0_.TEAM_NAME as team_name2_0_0_ 
    	from
        	team team0_ 
    	where
        	team0_.TEAM_ID=?
        	

	* 컬렉션 페치 조인(Collection fetch join)
	 - 일대다 관계, 컬렉션 페치 조인
	 - [JPQL] -> select t from team t join fetch t.teammates where t.name = 'Los Angeles Lakers'
	 - [SQL] -> select t.*, tm.* from team t inner join teammate tm on t.id = tm.team.id wehre t.name = 'Los Angeles Lakers'
	
	* 페치 조인과 DISTINCT
	 - select distinct t from team t join fetch t.teammates where t.name = 'Los Angeles Lakers'
	 - SQL(실제 DB)에서는 중복을 제거하기 위해 distinct를 추가해서 쿼리를 날려주면 데이터가 완전히 같지 않으므로 데이터 중복제거가 실패한다.
	  하지만 JPQL에서는 SQL로 distinct 붙여서 보내주면 1차로 DB에서 데이터를 가지고 온다음 JPA가 자체적으로 컬렉션, 자바 애플리케이션에서 중복되는 엔티티를 제거해주는 기능이있다. 

 	* 페치 조인과 일반 조인의 차이
 	 - 일반 조인 실행시(SQL) 데이터가 완전이 같지 않다면 연관된 엔티티를 완전히 조회해 오지 못한다.
 	 - JPQL은 결과를 반환해줄 때 연관관계를 고려해서 반환해주지 않는다.
 	 - 단지 SELECT 절에 지정한 엔티티만 조회할 뿐이다.
 	 - 일반 조인의 경우(join fetch X, join O) 팀 엔티티만 조회하고, 팀메이트 엔티티는 조회하지 않는다.
 	 
 	 - 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회해준다.(즉시 로딩), 원래 ManyToOne은 기본적으로 지연로딩 설정이 되어있음.
 	 - 페치 조인은 객체 그래프를 SQL 한번에 조회해주는 개념이다.
*/


