package com.mappedSuperclass;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.mappedSuperclass.dto.Buyer;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			Buyer buyer1 = new Buyer();
			buyer1.setName("박종희");
			buyer1.setCreatedBy("관리자");
			buyer1.setCreatedDate(LocalDateTime.now());
			
			em.persist(buyer1);
			
			em.flush();
			em.clear();
			
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
 	*@MappedSuperclass
 	- 공통 매핑 정보가 필요할 때 사용(id, name)
 	- 상속관계 매핑이 아니다.
 	- 엔티티가 아니고 테이블과 매핑이 안된다.
 	- 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공한다.
 	- 조회, 검색 불가(em.find(BaseEntity) 불가) -> 엔티티가 아니기 때문
 	- 직접 생성해서 사용할 일이 없으므로 추상 클래스(abstract)로 쓰는 것을 권장한다.
 	 * 추상클래스 : 클래스를 만들 때 abstract 키워드를 붙여서 해당 유형에 new 키워드를 쓸 수 없게한 클래스입니다. 이는 클래스 중에 인스턴스를 만들 수 없게 하기 위한 클래스를 만들기 위해 사용됩니다.
	- 테이블과 관계가 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할을 한다.
	- 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용한다.
    
    -> 참고로 jpa에서 @Entity 클래스에 상속이 가능한 경우는 @Entity가 붙어 있거나 @MappedSuperclass로 지정된 클래스만 상속이 가능하다. 
*/


