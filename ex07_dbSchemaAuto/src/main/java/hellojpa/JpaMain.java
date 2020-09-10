package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			Employee emp = em.find(Employee.class, 9); // find를 실행하면 영속 상태이다.(DB에서 데이터를 가지고와서 영속성 컨텍스트에 데이터가 올라가 있는 상태)
			emp.getId();
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
   6. 데이터베이스 스키마 자동 생성
    - DDL(Data Definition Language)을 애플리케이션 실행 시점에 자동 생성
    - 테이블 중심 -> 객체 중심
    - 데이터베이스 방언을 활용하여 데이터베이스에 맞는 적절한 DDL 생성
    - 이렇게 생성된 DDL은 개발 장비에서만 사용(운영 서버에선 X)
    - 생성된 DDL은 운영서버에서는 사용하지 않거나, 적절히 다듬은 후 사용
    
    6-1. 데이터베이스 스키마 자동 생성(속성) -> hibernate.hbm2ddl.auto
     - create : 기존 테이블 삭제 후 다시 생성(DROP + CREATE)
     - create-drop : create와 같은 속성이긴 하나 차이점은 종료 시점에 테이블을 DROP시킨다는게 차이점이다.
     - update : 변경분만 반영(운영 DB에서는 사용하면 안된다.)
     - validate : 엔티티와 테이블이 정상적으로 매핑되었는지만 확인(검증)해주는 기능.
     - none : 사용하지 않음.
    
    6-2. 데이터베이스 스키마 자동 생성(주의 사항) - 그냥 validate랑 none빼곤 평소에 사용하지 말아라 -
     - 운영 장비에서는 절대 hibernate.hbm2ddl.auto의 속성으로 create, create-drop, update 기능을 사용하면 안된다.
     - 개발 초기 단계에서는 create 또는 update만의 사용을 권장.
     - 테스트 서버에서 사용할 때는 update 또는 validate만의 사용을 권장.
     - 스테이징과 운영 서버에서는 validate 또는 none만의 사용을 권장.
 */
