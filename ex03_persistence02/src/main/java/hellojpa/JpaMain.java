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
		/*	// (1) 9번 10번을 진행하고 find로 검색하고 로그를 보면 select 쿼리가 안나감(이유 : 9번 10번 진행 후 1차 캐시에 데이터가 남아있기 때문에 DB에 직접 접근하지 않고 1차 캐시에 남아있는 데이터를 가지고 오기 때문에 select를 안하기 때문)
			// 9. 비영속 상태
			Employee emp = new Employee();
			emp.setId(7);
			emp.setName("박종국");
			// 
			
			// 10. 영속 상태
			System.out.println("=== before ===");
			em.persist(emp);
			System.out.println("=== after ===");
			//
			
			Employee findEmp = em.find(Employee.class, 7);
			
			System.out.println("findEmp.id = " + findEmp.getId());
			System.out.println("findEmp.name = " + findEmp.getName());
		*/
			// (2) (1)번 과정을 주석처리하고 새로 시작할 경우(select쿼리가 한 번만 나감 -> findEmp1를 생성하고 find를 할때 DB를 접근하고 1차 캐시에 데이터를 저장한다. 그 다음 findEmp2를 생성하고 find할때는 현재 같은 프로그램에서 돌아가고 있으니 1차 캐시에 찾으려는 데이터가 저장되어 있기 때문에 굳이 select 쿼리를 안날리고 1차 캐시에서 검색해서 데이터를 반환한다.)		
			// 7분 51초
//			Employee findEmp1 = em.find(Employee.class, 7);
//			Employee findEmp2 = em.find(Employee.class, 7);
//			
//			System.out.println("findEmp.id = " + findEmp1.getId());
//			System.out.println("findEmp.name = " + findEmp1.getName());
//			
//			System.out.println("findEmp.id = " + findEmp2.getId());
//			System.out.println("findEmp.name = " + findEmp2.getName());
//			
//			System.out.println("result = " + (findEmp1 == findEmp2));  // 영속 엔티티의 동일성 보장(1차 캐시 덕분)
			
//			Employee emp1 = new Employee(7, "박연주");
//			Employee emp2 = new Employee(8, "박서준");
//			
//			// persist 부분에서는 데이터를 영속성 컨텍스트에 쌓아 놓는다.(쿼리가 아직 안나감)
//			em.persist(emp1);
//			em.persist(emp2);
//			
//			System.out.println("=================");
			
			Employee emp = em.find(Employee.class, 8);
			
			emp.setName("손동하");
			
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
  *JPA에서 가장 중요한 2가지 : 객체와 관게형 데이터베이스(RDB) 매핑(Object Relational Mapping)
  
  1. 영속성 컨텍스트 
   -> JPA를 이해하는데 가장 중요한 용어.
   -> "엔티티(객체)를 영구 저장하는 환경"이라는 뜻.
   -> EntityManager.persist(entity);
   -> 영속성 컨텍스트는 논리적인 개념.
   -> 눈에 보이진 않는다.
   -> 엔티티 매니저 팩토리에서 꺼내온 엔티티 매니저를 통해서 영속성 컨텍스트에 접근한다.  	
   
  2. 엔티티의 생명주기
   - 비영속(new/transient) : 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태( ex) 단순히 new로 객체를 생성만 한 상태.) 
   - 영속(managed) : 영속성 컨텍스트에 관리되는 상태( ex) Employee emp = new Employee();로 emp라는 Employee형 객체 emp를 만들고 그것을 em.persist(emp); 하면 영속(managed)상태라고 한다. )	
 
  3. 영속성 컨텍스트의 이점 => 간단히 말해 중간 매개체가 있다는 의미.
   - 1차 캐시
    -> 처음 데이터를 조회할 때 데이터베이스를 접근하고 거기서 얻어온 데이터를 1차 캐시에 저장해놓는다.
    
   - 동일성(identity) 보장
    -> 1차 캐시로 인하여 객체를 두 번 선언해도 영속성 컨텍스트에 처음 객체를 선언할 때 데이터베이스에 접근해서 가져온 데이터를 저장해놓기 때문에 동일성을 보장해준다.
   
   - 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
    -> 엔티티 매니저는 데이터 작업을 위해서는 하나의 트랜잭션 안에서 시작해야 된다.
    -> 트랜잭션 안에서 insert(persist)를 했어도 insert SQL을 바로 데이터베이스에 보내지 않는다.
    -> 그걸 JPA가 쌓아 놨다가 마지막에 커밋을 할떄 데이터베이스로 SQL을 보낸다.
    -> JPA는 영속성 컨텍스트 안에 1차 캐시만 있는게 아니라 쓰기 지연 SQL 저장소라고 persist로 데이터가 insert 되면
    -> 일단 1차 캐시에 데이터를 먼저 저장해 놓고 그 다음 쓰기 지연 SQL 저장소에 데이터에 맞는 insert 쿼리를 생성해서 저장해 놓는 저장소가 있다.
    
   - 변경 감지(Dirty Checking) => 엔티티 수정
    -> 1차 캐시에는 데이터베이스에서 가져온 데이터를 저장하고 또 스냅샷(복사본)을 하나 만들어 놓는다.
    -> 영속성 컨텍스트에서는 어떤 데이터를 변경을 하고 커밋을 하면 내부적으로 flush라는게 호출되고 데이터객체랑 처음에 스냅샷을 떠놓은거랑 비교를 한다.
    -> 데이터가 서로 다르다면 쓰기 지연SQL 저장소에 정의되어 있는 update 쿼리를 데이터베이스 최신 데이터를 반영해준다.
    
   - 지연 로딩(Lazy Loading)
   
   *플러시(flush) => 커밋시 자동 발생
    -> 영속성 컨텍스트에서의 변경 내용(수정,삭제)을 데이터베이스에 최종적으로 반영해주는 녀석.
   
   *플러시가 발생되면 무슨일이 벌어지는가?
    1) 변경 감지(Dirty Checking)
    2) 수정된 엔티티를 쓰기 지연 SQL저장소에 등록
    3) 쓰기 지연 SQL저장소에 쌓아 놓은 쿼리를 데이터베이스에 전송(insert,update,delete)
    
   *영속성 컨텍스트를 플러시하는 방법
    1) em.flush(); -> 직접 호출
    2) 트랜잭션 커밋 -> 플러시 자동 호출
    3) JPQL(Java Persistence Query Language) 쿼리 실행 시 -> 플러시 자동 호출

 */
