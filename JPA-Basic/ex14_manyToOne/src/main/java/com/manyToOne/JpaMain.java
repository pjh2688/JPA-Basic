package com.manyToOne;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.manyToOne.dto.Master;
import com.manyToOne.dto.Servant;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			Master master01 = new Master();
			master01.setName("SAMSUNG");
		
			Master master02 = new Master();
			master02.setName("LG");
			
			em.persist(master01);	
			em.persist(master02);
			
			
			Servant servant01 = new Servant();
			servant01.setName("과장");
			servant01.setMaster(master01);  // 연관 관계 주인 매핑
			
			Servant servant02 = new Servant();
			servant02.setName("부장");
			servant02.setMaster(master02);
			
			em.persist(servant01);
			em.persist(servant02);
			
			master01.getServants().add(servant01);  // 양방향 매핑 방법 1
			master02.getServants().add(servant02);
			
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
 	*연관 관계 매핑시 고려사항 3가지
 	(1) 다중성(multiplicity) : N:1, 1:N, 1:1, N:M
 	 - 다대일 : @ManyToOne = 실무에서 가장 많이 쓰인다.
 	 - 일대다 : @OneToMany
 	 - 일대일 : @OneToOne
 	 - 다대다 : @ManyToMany => 실무에서 쓰지 않는다(금지).
 	
 	(2) 단방향, 양방향
 	 - 테이블 
 	  -> 외래 키 하나로 양쪽 조인 가능.
 	  -> 사실 방향이라는 개념이 없다.
 	  
 	 - 객체
 	  -> 참조용 필드가 있는 쪽으로만 참조 가능.
 	  -> 한쪽만 참조하면 단방향
 	  -> 양쪽이 서로 참조하면 양방향.
 	  
 	(3) 연관관계의 주인(양방향 일때)
 	 - 테이블은 외래키 하나로 두 테이블이 연관관계를 맺음.
 	 - 객체 양방향 관계는 A->B, B->A 처럼 A,B 사이에 참조(화살표가) 2개.
 	 - 객체 양방향 관계는 A,B 두 객체 사이에 참조(화살표)가 2개가 존재하기 때문에 두 객체 중 실제 테이블의 외래 키를 관리할 곳을 지정해 줘야 한다.
 	 - 연관관계의 주인 : 이처럼 A,B 두 객체 사이에서 테이블의 외래 키 값을 저장한 변수가 들어가 있는 객체(외래키를 관리하는 곳).
 	 - 연관관계 주인의 반대편 : 외래키에 영향을 주지 않고 데이터를 단순 조회만 가능하다.
 	 
 	*@ManyToOne(다대일 관계 매핑)
 	 - 주요 속성
 	  (1) optional : false로 설정하면 연관된 엔티티가 항상 있어야 한다.(기본값 : TRUE)
 	  (2) fetch : 글로벌 페치 전략을 설정한다.(기본값 : @ManyToOne=FetchType.EAGER , @OneToMany=FetchType.LAZY)
 	  (3) cascade : 영속성 전이 기능을 사용한다.
 	  (4) targetEntity : 연관된 엔티티의 타입 정보를 설정한다. 이 기능은 거의 사용하지 않는다. 컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있다.
*/


