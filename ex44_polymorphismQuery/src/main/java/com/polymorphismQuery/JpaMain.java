package com.polymorphismQuery;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.polymorphismQuery.dto.Item;
import com.polymorphismQuery.dto.Book;
import com.polymorphismQuery.dto.Movie;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {
			Book book = new Book();
			book.setName("신");
			book.setAuthor("베르나르 베르베르");
			
			em.persist(book);
			
			Movie movie = new Movie();
			movie.setName("테넷");
			movie.setDirector("크리스토퍼 놀란");
			
			em.persist(movie);
			

			em.flush();
			em.clear();
			
			
			// 1. TYPE 
//			String query = "select i from Item i where type(i) in (Book, Movie)";
	
			// 2. TREAT(JPA 2.1) - 다운 캐스팅
			String query = "select i from Item i where treat(i as Book).author = '베르나르 베르베르'";
			
			List<Item> result = em.createQuery(query, Item.class).getResultList();
			
			for(Item item : result) {
				System.out.println("item : " + item.getName());
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
 	* 다형성 쿼리 
 	(1) TYPE
 	 - 조회 대상을 특정 자식으로 한정.  
 	 ex) Item 중에 Book, Movie를 조회.
 	 
 	 - [JPQL] : select i from Item i where type(i) in (Book, Movie)
 	 
 	 - [SQL] : select i from Item i where i.DTYPE in ('Book', 'Movie')
 	 
 	(2) TREAT(JPA 2.1)
 	 - 자바의 타입 캐스팅과 유사
 	 - 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용
 	 - FROM, WHERE, SELECT 사용(하이버네이트에서 지원)
 	 
 	 ex) 부모 클래스 Item과 자식 클래스 Book이 있다면
 	 
 	 - [JPQL] : select i from Item i where treat(i as Book).author = '베르나르 베르베르'
*/


