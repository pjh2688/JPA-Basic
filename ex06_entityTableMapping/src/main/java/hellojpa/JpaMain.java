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
			System.out.println(emp.getId());
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
 
   5. 엔티티 매핑
    1) 객체와 테이블 매핑 : @Entity, @Table
     -> @Entity
      - @Entity 어노테이션이 붙어있는 클래스는 JPA가 관리하는 엔티티.
      - JPA를 사용하여 테이블과 클래스를 매핑할 때는 @Entity 어노테이션이 꼭 들어가야 한다.
     
     -> 주의할 점. 
      - 기본 생성자는 필수이다.(파라미터(매개변수)가 없는 public 또는 protected 생성자)
      - final이 붙은 클래스, enum, interface, inner 클래스를 사용하면 안된다. -> @Entity를 붙여서 매핑을 할 수 없다는 의미 (enum[enumerated type]은 열거형이라고 부른다. 열거형은 서로 연관된 상수들의 집합이라고 할 수 있다.)
      - 저장할 핋드에 final을 사용하면 안된다.
     
     -> @Entity 안에 있는 속성
      - name 속성 
       1) JPA에서 사용할 엔티티 이름을 지정한다.
       2) 기본값은 @Entity 어노테이션을 붙인 클래스 이름이다.
       3) 같은 클래스 이름이 없으면 가급적 기본값은 @Entity가 붙은 클래스명이다.
     
     -> @Table 안에 있는 속성
      - name 속성 : 매핑할 테이블 이름(기본값은 어노테이션이 붙은 엔티티 이름을 사용한다.)
      - catalog 속성 : 실제 데이터베이스의 catalog 매핑
      - schema 속성 : 데이터베이스 schema 매핑
      - uniqueConstraints(DDL) : DDL 생성 시에 유니크 제약 조건 생성
       
    2) 필드와 컬럼 매핑 : @Column
    3) 기본 키 매핑 : @Id
    4) 연관관계 매핑 : @ManyToOne, @JoinColumn
    
 */
