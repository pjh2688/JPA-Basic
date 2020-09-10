package com.jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jpql.dto.Partner;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {
			
//			Partner partner = new Partner();
//			partner.setName("박종희");
//			em.persist(partner);
			
			// 1. JPQL 안쓸 때
			Partner findpartner = em.find(Partner.class, 1L);
			System.out.println("JPQL 사용 안할 때  : " + findpartner.getName());
			
			// 2. JPQL 사용할 때 -> 주의 : jpql은 db쪽이아니라 entity 객체 쪽 이름을 써야된다. entity 객체에 등록된 name을 따로 설정하지 않았다면 클래스 명, 아니면 name에 등록된 이름을 정확히 명시해줘야 한다.
			List<Partner> result =  em.createQuery("select p from partner p where p.name like '%박%'", Partner.class).getResultList();
			
			for(Partner partner : result) {
				System.out.println("JPQL 사용 할 때 : " + partner.getName());
			}
			
			// 3-1. CriteriaBuilder 클래스를 생성하고 EntityManager에서 CriteriaBuilder 객체를 가져온다.
			CriteriaBuilder cb = em.getCriteriaBuilder();
			
			// 3-2. cb 객체에 있는 createQuery 메소드로 Partner 클래스 관련 객체에 관한 쿼리를 만들거라는 사실을 알려주고 반환 값을 CriteriaQuery<Partner> 형 변수 query에 저장한다. 
			CriteriaQuery<Partner> query =  cb.createQuery(Partner.class);
			
			// 3-3. 3-2에서 Partner 클래스를 대상으로 한 쿼리를 만들거라는걸 사전에 통보했으니 그 만들어진 query 틀에다가 from 메소드로 from절을 뒤에 붙여주고 매개변수 값을 대상 테이블로 붙여준다. -> select * from partner;의 효과. 일단 엔티티 데이터를 전체 select
			Root<Partner> p = query.from(Partner.class);
			
			// 3-4. 3-2, 3-3에선 틀을 만들었다고 생각하면 된다. 3-4에선 그 틀을 가지고 실제로 원하는 쿼리를 뽑아내는 작업이다. query 객체에서 뽑아낸 완성되지 않은 select 메소드 틀에다가 검색조건을 달아주기 위해 where 메소드를 사용해 p 객체에서 name 속성의 데이터를 얻어와 옆에 있는 문자값과 비교해 같은 데이터를 반환 시켜준다.
			CriteriaQuery<Partner> cq = query.select(p).where(cb.equal(p.get("name"), "박종희"));
			
			// 3-5.
			List<Partner> resultList = em.createQuery(cq).getResultList();
			
			// 3-6.
			for(Partner partner : resultList) {
				System.out.println("Criteria 사용 할 때  : " + partner.getName());
			}
			
			// 4-1. 네이티브 쿼리(주의 : 네이티브 쿼리 사용시 createNativeQuery안에 엔티티 객체 타입 클래스를 명시 안해주면 오류뜬다.)
			@SuppressWarnings("unchecked")
			List<Partner> nativeResult = em.createNativeQuery("select partner_id, partner_name from partner", Partner.class).getResultList();
			
			// 4-2. 
			for(Partner partner : nativeResult) {
				System.out.println("네이티브 쿼리 사용 할 때  : " + partner.getName());
			}
			
			// 5-1.
//			Partner partner = new Partner();
//			partner.setName("박규란");
//			em.persist(partner);
			
			// 5-2. flush -> commit할 때, Query 날라갈 때
			@SuppressWarnings("unchecked")
			List<Partner> listResult = em.createNativeQuery("select partner_id, partner_name from partner", Partner.class).getResultList();
			
			// 5-3 강제로 수동 flush
			em.flush();
			
			for(Partner pt : listResult) {
				System.out.println("partner : " + pt.getName());
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
   * 객체지향 쿼리 언어 소개
    - JPA는 다양한 쿼리 방법을 지원
     1) JPQL[Jakarta Persistence Query Language]
     2) JPA Criteria
     3) QueryDSL[Query Database Sub Language] 
	  -> 오픈소스 프로젝트다.
	  -> 일반적으로 복잡한 Creteria를 대체하는 JPQL 빌더다.
	  -> JPA의 표준스펙이 아니므로 약간의 설정이 더 필요하다.
	  -> 복잡한 쿼리와 동적쿼리를 깔끔하게 해결해준다.
	  -> 쿼리를 자바 코드로 작성할 수 있다. 따라서 문법오류를 컴파일단계에서 잡아줄 수 있다.
	 => 2),3)은 java로 코드를 짜서 JPQL을 빌드해주는 generator 클래스들의 모임.
	 4) 네이티브 SQL : 특정 데이터베이스에 종속하는 쿼리를 날려야 할때 쌩쿼리를 날릴 수 있게 해준다.
	 5) JDBC API 직접 사용, MyBatis와 SpringJdbcTemplate 함께 사용.
	
    1-1) JPQL 소개
     - 가장 단순한 조회 방법
      (1) EntityManager.find();
      (2) 객체 그래프 탐색(ex) a.getB().getC();)
     
     ex) 나이가 18살 이상인 회원을 모두 검색하고 싶다면?
     
    1-2) JPQL이 필요한 이유
     - JPA를 사용하면 엔티티 객체를 중심으로 개발을 한다.
     - 문제는 검색 쿼리에서 발생한다.
     - 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색하게 된다.
     - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능.
     - 애플리케이션이 필요한 데이터만 DB에서 꺼내오려면 결국 검색 조건이 포함된 SQL이 필요하다.    
     - 그리하여 JPA는 SQL을 추상화한 JPQL이라는  객체지향쿼리언어를 만들어서 제공해준다.
     - JPQL은 SQL과 문법이 유사하며 SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN을 지원한다.
     - JPQL은 테이블을 대상으로 하는 것이 아니라 엔티티 객체를 대상으로 하는 쿼리 언어이다.
     - SQL은 데이터베이스 테이블을 대상으로 하는 쿼리 언어이다.
    
    1-3) JPQL 특징
     - 테이블이 아닌 객체(Entity)를 대상으로 검색하는 객체 지향 쿼리
     - SQL을 추상화해서 만들어지기 때문에 특정 데이터베이스 SQL에 의존하지 않는다.
     - JPQL을 한마디로 정의하면 객체 지향 SQL.
     
   	2) JPA Criteria 소개
     - 문자가 아닌 java언어 코드로 JPQL을 작성할 수 있다.
     - JPQL 빌더 역할 -> 자바 코드로 JPQL을 만들어 낼 수 있다는 말.
     - JPA 공식 기능.
     - 단점 : 너무 복잡하고 실용성이 없다.
     - Criteria 대신에 QueryDSL 사용을 권장.
    
    3) QueryDSL 소개
     - 문자가 아닌 자바코드로 JPQL을 작성할 수 있다.
     - JPQL 빌더 역할 -> 자바 코드로 JPQL을 만들어 낼 수 있다는 말.
     - 컴파일 시점에 문법 오류를 찾아 낼 수 있다.
     - 동적 쿼리 작성시 편리하다.
     - 단순하고 쉽다.
    - 쉴무에서 사용을 권장한다.
    
    4) 네이티브 SQL 소개
     - JPA가 제공하는 SQL을 직접 사용하는 기능.
     - JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능을 사용할 때.
     ex) 오라클의 CONNECT BY, 특정 DB만 사용하는 SQL 힌트
     
    5) JDBC API 직접 사용, MyBatis와 SpringJdbcTemplate 함께 사용
     - JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링에서 제공하는 JDBCTemplate, Mybatis 등을 함께 사용 가능.
     - 단, 영속성 컨텍스트를 적절한 시점에 강제로 플러시 해주어야 한다.
     ex) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트를 수동으로 flush(); 
     
     
     <참고> @SuppressWarnings 어노테이션(annotation) 속성
	 - all : 모든 경고.  
	 - cast : 캐스트 연산자 관련 경고.
	 - dep-ann : 사용하지 말아야 할 주석 관련 경고.
	 - deprecation : 사용하지 말아야 할 메서드 관련 경고.
	 - fallthrough : switch문에서 break 누락 관련 경고.
	 - finally : 반환하지 않는 finally 블럭 관련 경고.
	 - null : null 분석 관련 경고.
	 - rawtypes : 제너릭을 사용하는 클래스 매개 변수가 불특정일 때의 경고.
	 - unchecked : 검증되지 않은 연산자 관련 경고.
	 - unused : 사용하지 않는 코드 관련 경고.

     
*/    


