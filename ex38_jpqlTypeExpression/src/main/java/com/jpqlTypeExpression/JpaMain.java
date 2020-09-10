package com.jpqlTypeExpression;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.jpqlTypeExpression.dto.Colleague;
import com.jpqlTypeExpression.dto.ColleagueType;
import com.jpqlTypeExpression.dto.Division;


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
			colleague.setType(ColleagueType.ADMIN);
			
			colleague.setDivision(division);
			
			em.persist(colleague);
			
			em.flush();
			em.clear();
			
			// 1. 일반(주의 : where 앞에 빈칸 조심 띄워쓰기 안되서 오류나는 경우도 있음)
//			String query = "select c.name, 'HELLO', TRUE from colleague c " + "where c.type = com.jpqlTypeExpression.dto.ColleagueType.ADMIN"; // 존재하닌까 조회를 하고 1을 반환
//			String query = "select c.name, 'HELLO', TRUE from colleague c " + "where c.type = com.jpqlTypeExpression.dto.ColleagueType.USER";  // 없으면 조회를 못하고 0을 반환
			
			// 2. 파라미터 바인딩 사용(패키지명 전부 안적고 싶을때)
//			String query = "select c.name, 'HELLO', TRUE from colleague c " + "where c.type = :userType";
	
			// 3. IS NOT NULL
//			String query = "select c.name, 'HELLO', TRUE from colleague c " + "where c.name is not null";
			
			// 4. BETWEEN
//			String query = "select c.name, 'HELLO', TRUE from colleague c " + "where c.age between 10 and 30";
			
			// 5. LIKE
			String query = "select c.name, 'HELLO', TRUE from colleague c " + "where c.age like 30";
			
			List<Object[]> result = em.createQuery(query).getResultList();
//			List<Object[]> result = em.createQuery(query)
//									.setParameter("userType", ColleagueType.ADMIN)
//									.getResultList();
			
			System.out.println("result = " + result.size());
			
			for(Object[] objects : result) {
				System.out.println("objects[0] = " + objects[0]);
				System.out.println("objects[1] = " + objects[1]);
				System.out.println("objects[2] = " + objects[2]);
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
 	* enum이란? 
 	 -> 관련이 있는 상수들의 집합이다. 자바에서는 final로 String과 같은 문자열이나 숫자들을 나타내는 
 	 	기본 자료형의 값을 고정할 수 있다. 이렇게 고정된 값을 상수라고 한다.
 	 	
 	* JPQL 타입 표현
 	 - 문자 : 'HELLO', 'She''s' => 특수 문자 쓰고 싶으면 문자하나 더 써주면 됨
 	 - 숫자 : 10L(Long), 10D(Double), 10F(Float)
 	 - Boolean : TRUE, FALSE
 	 - ENUM : 패키지명.ADMIN(패키지명을 모두 적어줘야 한다)
 	 - 엔티티 타입 : TYPE(m) = Member(상속 관계에서 사용)
 	 
 	* JPQL 기타
 	 - SQL과 문법이 같은 식
 	 - EXISTS, IN
 	 - AND, OR, NOT
 	 - =, >, >=, <, <=, <>
 	 - BETWEEN, LIKE, IS NULL
*/


