package com.jpqlNamedQuery;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.jpqlNamedQuery.dto.Team;
import com.jpqlNamedQuery.dto.Teammate;

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
			
			// 1. Named Query[어노테이션에 정의] -> 사용하려는 엔티티에 @NamedQuery 어노테이션을 지정한 후 정적 쿼리 생성
			// 2. Named Query[XML에 정의] -> 1번 어노테이션을 주석처리한 뒤 실행하자, ormTeammate.xml을 META-INF에 만들고 양식대로 작성한 뒤 persistence.xml에 <mapping-file>META-INF/ormTeammate.xml</mapping-file> 경로를 적어준다. <property> 보다 위에 위치해야 오류가 안난다.
			
			List<Teammate> resultList =	em.createNamedQuery("teammate.findByName", Teammate.class)
								.setParameter("name", "Kawhi Leonard")
								.getResultList();
			
			
			for(Teammate tm : resultList) {
				System.out.println("teammate.name = " + tm.getName());
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
  
 	* Parsing(파싱)
 	-> 언어 해석기인 컴파일러 또는 인터프리터가 프로그램을 이해하여 기계어로 번역하는 과정의 한 단계로, 
 	    각 문장의 문법적인 구성 또는 구문을 분석하는 과정. 즉, 원시 프로그램에서 나타난 토큰의 열을 받아들여 
 	    이를 그 언어의 문법에 맞게 구문 분석 트리(parse tree)로 구성해 내는 일이다. 
 	    크게 하향식 문장 분석과 상향식 문장 분석으로 나눌 수 있다.

 	* Named 쿼리 - 정적 쿼리
 	 - 미리 정의해서 이름을 부여해두고 사용하는 JPQL[Java Persistence Query Language]
 	 - 정적 쿼리
 	 - 어노테이션, XML파일에 정의
 	 - 애플리케이션 로딩 시점에 초기화 후 재사용
 	 - 애플리케이션 로딩 시점에 쿼리를 검증할 수 있다. -> 로딩 시점에 쿼리가 오타가 있거나 하면 해당 오류를 표시해준다.
 	 
 	* Named 쿼리 환경에 따른 설정
 	 - XML이 항상 우선권을 가진다.
 	 - 애플리케이션 운영 환경에 따라 다른 XML을 배포할 수 있다. 
*/


