package com.queryAPI;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.queryAPI.dto.Colleague;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			Colleague colleague = new Colleague();
			colleague.setName("박종희");
			colleague.setAge(30);
			
			em.persist(colleague);
			
			TypedQuery<Colleague> query1 = em.createQuery("select c from colleague c", Colleague.class);
			
			// 결과 조회 API 1
			List<Colleague> resultList1 = query1.getResultList();
			
			// 여러 개
			for(Colleague colleague1 : resultList1) {
				System.out.println("colleague1 = " + colleague1.getName());
			}

			TypedQuery<String> query2 = em.createQuery("select c.name from colleague c", String.class);
			
			// 결과 조회 API 2
			List<String> resultList2 = query2.getResultList();
			
			// 한 개 속성
			System.out.println("resultList2 = " + resultList2);
			
			// select 시 반환 타입이 서로 다를 경우 TypeQuery를 못 쓴다. Type정보가 다르기 때문에 안된다는 말.
		 // TypedQuery<String> query3 = em.createQuery("select c.name, c.age from colleague c", String.class); // -> X 하나는 반환 타입이 String이고 하나는 int

			// 결과 조회 API 3
			// query3의 경우 TypedQuery가 아니라 Query를 사용해야 한다.
			Query query4 = em.createQuery("select c.name, c.age from colleague c");
			
			@SuppressWarnings("unchecked")
			List<Object[]> resultList3 = query4.getResultList();
			
			for(Object[] row : resultList3) {
				String name = (String)row[0];
				Integer age = (Integer)row[1];
				
				System.out.println("이름 : " + name + ", 나이 : " + age);
			}
			
			// 파라미터 바인딩 - 이름 기준, 위치 기준
			TypedQuery<Colleague> query5 = em.createQuery("select c from colleague c where c.name = :name", Colleague.class);
			query5.setParameter("name", "박종희");
			
			Colleague singleResult = query5.getSingleResult();
			System.out.println("singleResult = " + singleResult.getName());
			
			// 위예제 메소드 체인 방식으로 변경 
			Colleague chainResult = em.createQuery("select c from colleague c where c.name = :name", Colleague.class).setParameter("name", "박종희").getSingleResult();
			
			System.out.println("chianResult.name : " + chainResult.getName());
			
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
  *JPQL[Java Persistence Query Language]
   (1) JPQL 소개
    - JPQL은 객체지향 쿼리 언어이다. 따라서 테이블을 대상으로 쿼리하는 것이 아니라 엔티티 객체를 대상으로 쿼리한다.
    - JPQL은 SQL을 추상화해서 만든 언어이기 때문에 특정 데이터베이스 SQL에 의존하지 않는다.
    - JPQL은 결국 SQL로 변환된다.
    
   (2) JPQL 문법 -> ex) select m from Member as m where m.age > 18
    - 엔티티와 속성은 대소문자를 구분한다.
    - JPQL 키워드는 대소문자를 구분하지 않는다.(SELECT, select, FROM, from, WHERE, where)
    - 엔티티는 이름을 사용한다. 테이블 이름이 아님(Colleague)
    - 별칭을 필수(m), as는 생략가능.
   
   (3) 집합과 정렬 메소드가 제공된다. 
   	- 집합 : COUNT(c), SUM(m.age), AVG(m.age), MAX(m.age), MIN(m.age)
    - 정렬 : GROUP BY, HAVING, ORDER BY
    
   (4) TypeQuery, Query
    - TypeQuery : 반환 타입이 명확할 때 사용.
     ex) TypedQuery<Colleague> query = em.createQuery("SELECT c FROM colleague c", Colleague.class);
     
    - Query : 반환 타입이 명확하지 않을 때 사용.
     ex) ex) Query query = em.createQuery("SELECT c.id, c.name FROM colleague c");
     
   (5) 결과 조회 API
    - query.getResultList() : 결과가 하나 이상일 때, 리스트 반환.
     * 결과가 없으면 빈 리스트를 반환해주기 때문에 nullpointexception이 발생하지 않는다.
    
    - query.getSingleResult() : 결과가 정확히 하나가 나와야 한다, 단일 객체 반환.
     * 결과가 없으면  : javax.persistence.NoResultException
     * 결과가 둘 이상이면 : javax.persistence.NonUniqueResultException
*/


