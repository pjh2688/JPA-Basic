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
			
//			Member member = new Member();
//			member.setUsername("C");
//			
//			em.persist(member);
			
			Member member1 = new Member();
			member1.setUsername("A");
			
			Member member2 = new Member();
			member2.setUsername("B");
			
			Member member3 = new Member();
			member3.setUsername("C");
			
			System.out.println("==============");
			em.persist(member1);  // (1, 51) member1만 주석을 풀고 member2,member3를 주석처리 해주고 실행하면 "select member_seq.nextval from dual" 쿼리가 두 번 나가는 것을 볼 수 있다.(영속성 컨텍스트에 50개의 메모리를 확보한다고 생각하면 될거 같음)
			em.persist(member2);  // (memory=51) 여기서부터는 영속성 컨텍스트에 확보된 메모리에서 바로 꺼내 써서 속도가 빨라짐.
			em.persist(member3);  // (memory=51)
			
			System.out.println("member1 = " + member1.getId());
			System.out.println("member2 = " + member2.getId());
			System.out.println("member3 = " + member3.getId());
			
			System.out.println("==============");
			
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
	8. 기본 키 매핑
	- 기본 키 매핑 어노테이션
	 1) @Id
	 2) @GeneratedValue
	 
	- 기본 키 매핑 방법
	 1) 직접 할당 : @Id만 사용
	 2) 자동 생성 : @GeneratedValue 사용
	  *속성
	   (1) IDENTIFY : 기본키 생성을 데이터베이스에 위임, MYSQL
	   (2) SEQUENCE : 데이터베이스 시퀀스 오브젝트 사용, ORACLE -> @SequenceGenerator 필요
	   (3) TABLE : 키 생성용 테이블 사용, 모든 데이터베이스에서 사용 -> @TableGenerator 필요
	   (4) AUTO : 각 데이터베이스 방언에 따라 자동 지정, 기본값
	
	- 권장하는 기본 키 매핑 전략
	 1) 기본 키 제약 조건 : null(x), 유일성, 불변성.
	 2) 미래까지 이 조건을 만족하는 자연키는 찾기 어렵기 때문에 대체키(대리키)를 사용.
	 3) 예를 들어 주민등록번호도 기본 키로 적절하지는 않다.
	 4) 권장하는 방법 : Long Type + 대체키 + 키 생성 전략 사용.
 */
