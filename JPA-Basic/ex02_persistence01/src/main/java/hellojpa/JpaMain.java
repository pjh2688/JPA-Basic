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
		/*
			// 9. 비영속 상태
			Employee emp = new Employee();
			emp.setId(6);
			emp.setName("박종태");
			// 
			
			// 10. 영속 상태
			System.out.println("=== before ===");
			em.persist(emp);
			System.out.println("=== after ===");
			//
			
			// 11. 1차 캐시 이용 확인
			Employee findEmp = em.find(Employee.class, 6);
			System.out.println("findEmp.getId() = " + findEmp.getId());
			System.out.println("findEmp.getName() = " + findEmp.getName());
		*/
			
			// 12. 9~11 주석 처리하고 종료 후 새로 12번부터 시작
			// 13. 첫번째 조회할때는 데이터베이스에 한 번 접근해야 한다.(쿼리가 나감).
			Employee findEmp1 = em.find(Employee.class, 5);
			System.out.println("findEmp1.getId() = " + findEmp1.getId());
			System.out.println("findEmp1.getName() = " + findEmp1.getName());
			
			// 14. 두번째를 조회할때는 영속성컨텍스트에 캐시로 데이터가 저장되어 있기 때문에 여기서 바로 데이터를 읽는다.(쿼리가 안나감).
			Employee findEmp2 = em.find(Employee.class, 5); 
			System.out.println("findEmp2.getId() = " + findEmp2.getId());
			System.out.println("findEmp2.getName() = " + findEmp2.getName());
			
			// 2020-05-14 -> 영속성 컨텍스트 2 : 6분 44초부터
			
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
 *JPQL(Java Persistence Query Language)
* - JPA를 사용하면 엔티티 객체를 중심으로 개발.
* - 문제는 검색 쿼리.
* - 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색.
* - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능.
* - 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요.
* - JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공.
* - SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원.
* - JPQL은 엔티티 객체를 대상으로 쿼리.
* - SQL은 데이터베이스 테이블을 대상으로 쿼리.
* - 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리이다.
* - SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.(JPQL로 명령하면 내부에서 DB에 맞게 변환해준다는 말)
* - JPQL을 한마디로 정의하면 객체 지향 Structure Query Language.
*/

/*
  *JPA에서 가장 중요한 2가지
  1. 객체와 관게형 데이터베이스(RDB) 매핑(Object Relational Mapping)
  
  2. 영속성 컨텍스트 
   -> JPA를 이해하는데 가장 중요한 용어.
   -> "엔티티(객체)를 영구 저장하는 환경"이라는 뜻.
   -> EntityManager.persist(entity);
   -> 영속성 컨텍스트는 논리적인 개념.
   -> 눈에 보이진 않는다.
   -> 엔티티 매니저 팩토리에서 꺼내온 엔티티 매니저를 통해서 영속성 컨텍스트에 접근한다.  	
   
  3. 엔티티의 생명주기
   - 비영속(new/transient) : 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태( ex) 단순히 new로 객체를 생성만 한 상태.) 
   - 영속(managed) : 영속성 컨텍스트에 관리되는 상태( ex) Employee emp = new Employee();로 emp라는 Employee형 객체 emp를 만들고 그것을 em.persist(emp); 하면 영속(managed)상태라고 한다. )	
 
  4. 영속성 컨텍스트의 이점
   - 1차 캐시
   - 동일성(identity) 보장
   - 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
   - 변경 감지(Dirty Checking)
   - 지연 로딩(Lazy Loading)
   
    => 간단히 말해 중간 매개체가 있다는 의미.
 */
