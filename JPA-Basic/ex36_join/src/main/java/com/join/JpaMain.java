package com.join;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.join.dto.Colleague;
import com.join.dto.Division;

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
			division.setName("올원");
			
			em.persist(division);
			
			Colleague colleague = new Colleague();
			colleague.setName("박종희");
			colleague.setAge(30);
			
			colleague.setDivision(division);
			
			em.persist(colleague);
			
		//	String query = "select c from colleague c inner join c.division d";  // 1. inner join
			String query = "select c from colleague c left join c.division d";  //  2. left join
		//	String query = "select c from colleague c left outer join c.division d";  // 3. left outer join -> 2,3번은 같은 표현
		//	String query = "select c from colleague c, division d where c.name = d.name";  // 4. theta join
		//	String query = "select c from colleague c left join c.division d on d.name = '올원'"; // 5. on 으로 조인 대상 필터링
		//	String query = "select c from colleague c left join division d on c.name = d.name";  // 6. 연관 관계가 없는 엔티티들 outer join
			
			List<Colleague> result = em.createQuery(query, Colleague.class).getResultList();
			
			for(Colleague co : result) {
				System.out.println("colleague = " + co.getName());
				System.out.println("colleague = " + co.getAge());
				System.out.println("colleague = " + co.getDivision().getName());
			}
			
			
			em.flush();
			em.clear();
			
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
 	*JPA 조인(join)
 	 - 내부 조인(inner join) - A와 B를 inner-Join하면 A와 B의 교집합을 얻을 수 있습니다.
 	 - 외부 조인(outer join) - A와 B를 outer-Join하면 A와 B의 합집합을 얻을 수 있습니다.
 	 - 세타 조인(theta join) - 조인에 참여하는 두 릴레이션의 속성 값을 비교하여 조건을 만족하는 튜플만 반환.
*/


