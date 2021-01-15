package com.subQuery;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.subQuery.dto.Colleague;
import com.subQuery.dto.Division;


public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {
			Division division = new Division();
			division.setName("TeamA");
			
			em.persist(division);
			
			Colleague colleague = new Colleague();
			colleague.setName("박종희");
			colleague.setAge(30);
			
			colleague.setDivision(division);
			
			em.persist(colleague);
			
			em.flush();
			em.clear();
			
			String query = "select c from colleague c left join c.division d";
			
			List<Colleague> result = em.createQuery(query, Colleague.class).getResultList();
			
			System.out.println("result = " + result.size());
			
			for(Colleague co : result) {
				System.out.println("colleague = " + co.getName());
				System.out.println("colleague = " + co.getAge());
				System.out.println("colleague = " + co.getDivision().getName());
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
 	* 서브 쿼리(Sub Query)의 예
 	 - 나이가 평균보다 많은 회원(성능이 잘 나오는 경우) -> 따로따로
 	  ex) select c1 from colleague c1 
 	   	  where c1.age > (select avg(c2.age) from colleague c2;
 	   	  
 	 - 1 건이라도 주문한 고객(성능이 잘 안나오는 경우) -> 같이
 	  ex) select c from colleague c
 	  	  where (select count(o) from order o where c = o.colleague) > 0;

    * 서브 쿼리에서 지원하는 함수
     (1) [NOT] EXISTS (subquery) : 서브쿼리에 결과가 존재하면 참을 반환, 아니면 거짓을 반환.
      - subquery 앞에는 3 가지 조건이 붙을 수 있다. {ALL | ANY | SOME }
      - ALL 조건은 모두 만족할때만 참을 반환.
      - ANY, SOME 조건은 조건을 하나라도 만족하면 참을 반환.
      
     (2) [NOT] IN (subquery) : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참을 반환.
     
    * JPA 서브 쿼리의 한계
     (1) JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용이 가능하다.
     (2) 그렇지만 SELECT 절도 가능하다.(따로 하이버네이트에서 지원해준다.)
     (3) FROM 절의 서브 쿼리는 현재 JPQL(Java Persistence Query Language)에서는 불가능하다.
     (4) (3)은 JOIN으로 풀 수 있으면 풀어서 해결해야 한다. 
*/


