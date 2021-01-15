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
			
			// 조건식을 이용해 데이터를 select 해온다.(결과 값이 String이므로 List에 String 타입을 명시해줘야 한다.)
			// String query 작성할 때 띄워쓰기 주의 " " 안에 한꺼번에 다 쓸 떄는 띄워쓰기가 잘 보이는데  " "를 나눠서 쓸 때는 띄워쓰기를 잘 봐야 된다.
			
			// 1. 기본 CASE 식
//			String query = 
//					"select " +
//							"case when c.age <= 5 and c.age <= 13 then '어린이' " +
//							"	  when c.age <= 14 and c.age <= 19 then '청소년' " +
//							"	  else '일반' " +
//							"end " +
//					"from colleague c";
			
			// 2. COALESCE - 해당 속성에 값이 null이면 '이름 없는 회원'으로 반환.
//			String query = "select coalesce(c.name, '이름 없는 회원') from colleague c";
			
			// 3. NULLIF - 해당이름이 존재하면 NULL을 반환 그런 이름이 없다면 List에 담긴 name 속성들을 반환. ex) 관리자의 이름을 숨겨야 할 때 사용.
			String query = "select nullif(c.name, '관리자') " + 
						   "from colleague c";
			List<String> result = em.createQuery(query, String.class).getResultList();
			
			for(String s : result) {
				System.out.println("s = " + s);
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
 	*조건식 - CASE 식
 	 (1) 기본 CASE 식
 	 (2) 단순 CASE 식
 	 (3) COALESCE 식 : 하나씩 조회해서 null이 아니면 반환
 	 (4) NULLIF 식 : 두 값이 같으면 null 반환, 두 값이 다르면 첫번째 값을 반환.
*/


