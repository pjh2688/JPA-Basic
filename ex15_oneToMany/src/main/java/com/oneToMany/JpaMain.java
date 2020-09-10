package com.oneToMany;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.oneToMany.dto.Singer;
import com.oneToMany.dto.Song;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			Song song = new Song();
			song.setName("휘파람");
			em.persist(song);
			
			Singer singer = new Singer();
			singer.setName("이문세");
			singer.getSongs().add(song);
			
			em.persist(singer);
		
			tx.commit();  // 5. 정상적으로 오류없이 여기까지 왔다면 트랜잭션 커밋 .
			
		} catch(Exception e) {  // 6. 수행 도중 오류가 있다면 트랜잭션 롤백.
			tx.rollback();  
		} finally {  // 7. 5번을 정상적으로 수행했다면 em 객체 close
			em.close();  
		}
		// 여기는 데이터 처리하는 곳(끝)
		emf.close();   // 8. try-catch문을 정상으로 빠져나왔따면 emf 객체 close
	}

}
/*
 	*일대다 단방향 구조 정리
 	- 일대다 단뱡향은 일대다(1:N)에서 일(1)쪽이 연관관계의 주인이다.
 	- 테이블 일대다 관계는 항상 다(N)쪽에 외래 키가 있다.
 	- 객체와 테이블의 차이 때문에 반대편 테이블의 외래 키를 관리하는 특이한 구조이다.
 	- @JoinColumn을 꼭 사용해야 한다. 그렇지 않으면 조인 테이블 방식을 자동으로 사용하여 중간에 새로운 조인 테이블이 만들어진다.

	*일대다 단방향 매핑 구조의 단점
	- 외래키가 있는 엔티티 테이블이 다(N)쪽에 존재.
	- 연관관계 관리를 위해 추가로 UPDATYE SQL이 실행되어 외래키 컬럼을 생성해준다.
	
	정리 : 일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하자.
*/


