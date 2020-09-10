package com.jpqlFunction;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.jpqlFunction.dto.Colleague;
import com.jpqlFunction.dto.ColleagueType;
import com.jpqlFunction.dto.Division;




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
			
			Colleague colleague1 = new Colleague();
			colleague1.setName("박종희");
			colleague1.setAge(30);
			colleague1.setType(ColleagueType.ADMIN);
			
			colleague1.setDivision(division);
			
			Colleague colleague2 = new Colleague();
			colleague2.setName("박규란");
			colleague2.setAge(28);
			colleague2.setType(ColleagueType.ADMIN);
			
			colleague2.setDivision(division);
			
			em.persist(colleague1);
			em.persist(colleague2);
			
			em.flush();
			em.clear();
			
			// 기본
//			String query = "select c.name from colleague c";
			
			// 1. JPQL 기본 함수 - concat
//			String query = "select concat('a', 'b') from colleague c";
//			String query = "select 'a' || 'b' from colleague c";
			
			// 2. JPQL 기본 함수 - substring
//			String query = "select substring(c.name, 1, 2) from colleague c";  // 1번째부터 2번째까지만 잘라내서 출력해
			
			// 3. JPQL 기본 함수 - trim -> ex) colleague.setName(" 박종희 "); 부분에 앞 뒤로 공백이 들어가면 trim을 사용하면 공백을 제거해서 출력해준다.
//			String query = "select trim(c.name) from colleague c";
			
			// 4. JPQL 기본 함수 - LOWER -> 문자를 소문자로 바꿔주는 함수. 
//			String query= "select lower(c.division.name) from colleague c";
			
			// 5. JPQL 기본 함수 - UPPER -> 문자를 대문자로 바꿔주는 함수.
//			String query= "select upper(c.division.name) from colleague c";
			
/*
 			// 6. JPQL 기본 함수 - LENGTH
			String query= "select length(c.division.name) from colleague c";
			
			List<Integer> result = em.createQuery(query, Integer.class).getResultList();
			
			for(Integer i : result) {
				System.out.println(i);
			}
*/
/*
			// 7. JPQL 기본 함수 - LOCATE -> 박종희에서 종희를 찾으닌까 '종'이 2번째 요소에 있기 때문에 2를 반환해 준다.
			String query = "select locate('종희', '박종희') from colleague c";
			
			List<Integer> result = em.createQuery(query, Integer.class).getResultList();
			
			for(Integer i : result) {
				System.out.println(i);
			}
*/
/*			
			// 8. JPQL 기본 함수 - ABS -> 인자 값의 절대값을 반환해주는 함수
			String query = "select abs(-c.age) from colleague c";

			List<Integer> result = em.createQuery(query, Integer.class).getResultList();

			for (Integer i : result) {
				System.out.println(i);
			}
*/		
			// 9. JPQL 기본 함수 - SORT -> 예시를 찾아야함
		
			// 10. JPQL 기본 함수 - MOD -> 6:40초
			String query = "select mod(4, 3) from colleague c";
			
			List<Integer> result = em.createQuery(query, Integer.class).getResultList();

			for (Integer i : result) {
				System.out.println(i);
			}
			
//			List<String> result = em.createQuery(query, String.class).getResultList();
//			
//			for(String s : result) {
//				System.out.println("s = " + s);
//			}
			
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
 	*JPQL에서 제공하는 함수
 	1) JPQL 기본 함수 - 데이터베이스에 관계 없이 사용 가능
 	 - CONCAT
 	 - SUBSTRING
 	 - TRIM
 	 - LOWER, UPPER
 	 - LENGTH
 	 - LOCATE
 	 - ABS, SQRT, MOD
 	 - SIZE, INDEX(JPA 용도)
 	 
 	2) 사용자 정의 함수 
 	 - 하이버네이트는 사용전 방언에 추가해야 한다.
 	 - 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다.
*/


