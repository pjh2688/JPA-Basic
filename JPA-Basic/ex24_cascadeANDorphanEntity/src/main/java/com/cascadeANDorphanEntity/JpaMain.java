package com.cascadeANDorphanEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.cascadeANDorphanEntity.dto.Child;
import com.cascadeANDorphanEntity.dto.Parent;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			
			Child child01 = new Child();
			Child child02 = new Child();
			
			Parent parent = new Parent();
			parent.addChild(child01);
			parent.addChild(child02);
			
			em.persist(parent);
//			em.persist(child01);
//			em.persist(child02);
			
			em.flush();
			em.clear();

			Parent findParent = em.find(Parent.class, parent.getId());
			findParent.getChildren().remove(0);  // 영속성 컨텍스트에서 제거한다는 말. create모드로 돌리면 0번째 요소는 최종적으로 insert 안됨. delete 쿼리가 나가는걸 볼 수 있음.
			
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
   1. 영속성 전이[CASCADE]
    *영속성 전이란?
   	 -특정 엔티티를 영속 상태(persist)로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때 사용.
 	 ex) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장해야 하는 경우
 	 
 	*영속성 전이[CASCADE]를 사용할 때 주의 사항
 	 - 영속성 전의는 연관관계를 매핑하는 것과 아무런 관련이 없다.
 	 - 엔티티를 영속화(persist)할 때 연관관계 매핑이 되어있는 엔티티도 함께 영속화시켜주는 편리함을 제공할 뿐이다.
 	 
 	*CASCADE의 속성
 	 - ALL : 모두 적용
 	 - PERSIST : 영속
 	 - REMOVE : 삭제
 	 - MERGE : 병합
 	 - REFRESH : REFRESH
 	 - DETACH : DETACH
 
   2. 고아 객체[Orphan Entity]
    *특징
    (1) 고아 객체 제거 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제.
    (2) orphanRemoval = true
    (3) Parent parent1 = em.find(Parent.class, parent1.getId());
    	parent1.getChildren().remove(0);  // 자식 엔티티를 컬렉션에서 제거
    (4) delete from child where id = ?
	 
	*주의 사항
	(1) 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 판단하고 삭제시키는 기능.
	(2) 참조하는 곳이 하나일 때 사용해야만 한다.
	(3) 특정 엔티티가 개인 소유할 때 사용
	(4) @OneToOne, @OneToMany만 가능
	
	*참고 : 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화시키면, 부모를 제거할 때
	자식도 함께 제거된다. 이것은 CascadeType.REMOVE처럼 동작한다.
	
   3. 영속성 전이와 고아 객체 기능을 모두 활성화(생명주기)
    - CascadeType.ALL + orphanRemovel=true
    - 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화할 수 있고 em.remove()로 제거할 수 있다.
    - 두 옵션을 모두 활성화 시키면 부모 엔티티를 통해서 자식의 생명주기도 관리할 수 있다. -> 부모랑 자식 관계에서 부모만 persist하고 자식은 persist를 안해도 된다. 부모만 persist를 해도 자식은 자동으로 persist 된다는 말.
    - 도메인 주도 설계[Domain-Driven Design, DDD]의 Aggregate Root 개념을 구현할 때 유용하다.
*/


