package com.pathExpression;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.pathExpression.dto.Colleague;
import com.pathExpression.dto.ColleagueType;
import com.pathExpression.dto.Division;






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
			
			em.persist(colleague1);
			
			Colleague colleague2 = new Colleague();
			colleague2.setName("박규란");
			colleague2.setAge(28);
			colleague2.setType(ColleagueType.ADMIN);
			
			colleague2.setDivision(division);
			
			em.persist(colleague2);
			
			em.flush();
			em.clear();
			
			// 1. 상태 필드(state field)
//			String query = "select c.name from colleague c";
//			
//			List<String> result = em.createQuery(query, String.class).getResultList();
//			
//			for(String s : result) {
//				System.out.println("s = " + s);
//			}
			
			// 2. 연관 필드(association field) - 단일 값 연관 경로(묵시적 내부 조인(inner join) inner join 쿼리 확인용)
//			String query = "select c.division from colleague c";
//			
//			List<Division> result = em.createQuery(query, Division.class).getResultList();
//			
//			for(Division d : result) {
//				System.out.println("d = " + d);
//			}
			
			// 3. 연관 필드(association field) - 단일 값 연관 경로(2번과 똑같이 단일 값 연관 경로를 탐색하나 inner join이 아닌 일반적인 join 쿼리가 나간다.(묵시적))
//			String query = "select c.division.name from colleague c";
//		
//			List<String> result = em.createQuery(query, String.class).getResultList();
//			
//			for(String s : result) {
//				System.out.println("s = " + s);
//			}
			
			// 4. 연관 필드(association field) - 컬렉션 값 연관 경로(더이상 탐색이 안됨) -> 자바 컬렉션으로 분류되었기 떄문	
//			String query = "select d.colleagues from division d";  // "select d.colleagues.name from division d" => 경로 탐색이 더이상 안됨
//			
//			Collection result = em.createQuery(query, Collection.class).getResultList();
//			
//			for(Object o : result) {
//				System.out.println("o = " + o);
//			}
			
			// 5. 연관 필드(association field) - 컬렉션 값 연관 경로(더이상 탐색이 안되는 문제 해결) -> 별칭을 이용한 명시적 조인 사용
			String query = "select c.name from division d join d.colleagues c";
			
			List<String> result = em.createQuery(query, String.class).getResultList();
			
			for(String s : result) {
				System.out.println("s = " + s);
			}
			
			// 결론은 실무에서는 묵시적 조인을 사용하지 말고 명시적 조인을 사용하자. 
			
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
 	* 경로 표현식[Path Expression]
     - 정의 -> .(점)을 찍어 객체 그래프를 탐색하는 것.
      ex) select c.name -> 상태 필드
      	   from Colleague c
      	    join c.division d -> 단일 값 연관 필드
      	    join c.order o -> 컬렉션 값 연관 필드
      	  where d.name = 'teamA';
      	  
    * 경로 표현식 용어 정리
     1) 상태 필드(state field) : 단순히 값을 저장하기 위한 필드. 
     	ex) c.name
     	
     2) 연관 필드(association field) : 연관관계를 위한 필드
      - 단일 값 연관 필드
       -> @ManyToOne, @OneToOne, 대상이 엔티티(c.division)
      
      - 컬렉션 값 연관 필드
       -> @OneToMany, @ManyToMany, 대상이 컬렉션(c.orders) 
       
    * 경로 표현식 특징
     - 상태 필드(state field) : 경로 탐색의 끝, 탐색 X
     - 단일 값 연관 경로 : 묵시적 내부 조인(inner join) 발생, 탐색 O
     - 컬렉션 값 연관 경로 : 묵시적 내부조인(inner join) 발생, 탐색 X
     - FROM 절에서 명시적 조인을 통해 별칭을 얻으면 병칠을 통해 탐색 가능.
     
    * 상태 필드(state field) 경로 탐색 -> JPQL이랑 SQL이랑 동일
     1) JPQL : select c.name, c.age from colleague c
     2) SQL : select c.name, c.age from colleague c
     
    * 단일 값 연관 경로 탐색(쿼리가 2)처럼 나가니 복잡하닌까 쓰지 말라는 얘기)
     1) JPQL : select c.division from colleague c
     2) SQL 
     -> select d.* from colleague c 
     		inner join Division d
     		 on c.division_id = d.division_id
     		 
    * 명시적 조인, 묵시적 조인
     1) 명시적 조인 : join 키워드를 직접 사용(별칭을 이용하여)
      ex) select c.name from division d join d.colleagues c
     2) 묵시적 조인 : 경로 표현식에 의해 묵시적으로 SQL 조인 발생(내부 조인만 가능)
      ex) select c.division from colleague c
      
    * 경로 표현식 예제
     1) select c.division.name from colleague c -> 성공(단일 값 연관 경로)
     2) select d.colleagues from division d -> 성공(컬렉션 값 연관 경로)
     3) select d.colleagues.name from division d -> 실패(컬렉션 값 연관 경로(자바 컬렉션으로 분류되기 때문에 더이상 탐색이 안됨))
     4) select c.name from division d join d.colleagues c -> 성공(컬렉션 값 연관 경로(더이상 탐색이 안되는 문제 해결) -> 별칭을 이용한 명시적 조인 사용)
 	 
 	* 경로 탐색을 사용한 묵시적 조인 사용 시 주의 사항
 	 1) 항상 내부 조인이다.
 	 2) 컬렉션 타입은 탐색 시 경로 탐색의 끝을 가리키기 때문에, 명시적 조인으로 별칭을 얻어 사용해야 탐색이 가능
 	 3) 경로 탐색은 주로 SELECT, WHERE,절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM (JOIN) 절에 영향을 준다.
 	 
 	* 실무에서 사용 할 때
 	 1) 가급적 묵시적 조인 대신 명시적 조인을 사용.
 	 2) 조인은 SQL 튜닝에 중요 포인트이다.
 	 3) 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려워서 실무에선 좋지 못하다.
*/


