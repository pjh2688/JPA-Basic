package com.projection;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.projection.dto.Address;
import com.projection.dto.Colleague;
import com.projection.dto.Division;
import com.projection.dto.Order;
import com.projection.dto.Product;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			Division division = new Division();
			division.setName("컴퓨터공학부");
			
			em.persist(division);
			
			Colleague colleague = new Colleague();
			colleague.setName("박종희");
			colleague.setDivision(division);
			
			colleague.setAge(30);
			
			em.persist(colleague);
			
			em.flush();
			em.clear();
			
			// 1. 엔티티 프로젝션
			List<Colleague> result1 = em.createQuery("select c from colleague c", Colleague.class).getResultList();
			
			Colleague findColleague = result1.get(0);
			findColleague.setAge(20);
			
			System.out.println(findColleague.getAge());
			
			// 2. 엔티티 프로젝션 조인 
			List<Division> result2 = em.createQuery("select c.division from colleague c", Division.class).getResultList();
			
			System.out.println(result2.get(0).getName());
			
			// 3. 엔티티 프로젝션 조인(추천)
			List<Division> result3 = em.createQuery("select d from colleague c join c.division d", Division.class).getResultList();
			
			System.out.println(result3.get(0).getName());
			
			// 4. 임베디드 타입 프로젝션(Order 안에 있는 Address 값 타입)
			Order order = new Order();
			order.setAddress(new Address("청주시 상당구 ", "산성로 55", "탑동LH 104-1602"));
			
			em.persist(order);
			
			List<Address> result4 = em.createQuery("select o.address from order o", Address.class).getResultList();
			
			System.out.println(result4.get(0).getCity() + result4.get(0).getStreet() + result4.get(0).getZipcode());
			
			// 5. 상품 추가해보기(주의 : order에 직접 해버리면 product 엔티티는 영속성 컨텍스트에서 관리하는게 아니기 때문에 오류가 남 order에서 product에 접근할려고 하기 때문에(양방향 맵핑이 되어있어서))
			Product product = new Product();
			product.setName("불가리스");
			product.setPrice(1400);
			
			order.setProduct(product);
			
			em.persist(product);
			
			List<Product> result5 = em.createQuery("select p from product p", Product.class).getResultList();
			
			System.out.println(result5.get(0).getName() + "의 가격은 " + result5.get(0).getPrice() + "원 입니다.");
			
			// 6-1. 여러 값 조회 - Query 사용	
			Query query = em.createQuery("select distinct c.name, c.age from colleague c");
			
			@SuppressWarnings("rawtypes")
			List result6 = query.getResultList();
						
			Object o = result6.get(0);
			Object [] res = (Object[])o;
			
			System.out.println("6-1. 이름 = " + res[0] + ", 나이 = " + res[1]);

			
			// 6-2. 여러 값 조회 - List 데이터형을 Object[] 직접 받아 6-1보다 간결하게 
			@SuppressWarnings("unchecked")
			List<Object[]> result7 = em.createQuery("select distinct c.name, c.age from colleague c").getResultList();
			
			for(Object[] row : result7) {
				String name = (String)row[0];
				Integer age = (Integer)row[1];
				
				System.out.println("6-2. 이름 : " + name + ", 나이 : " + age);
			}
			
			// 6-3. 6-2의 다른 출력 방법.
			@SuppressWarnings("unchecked")
			List<Object[]> result8 = em.createQuery("select distinct c.name, c.age from colleague c").getResultList();
			
			Object[] result = result8.get(0);
			System.out.println("6-3. 이름 : " + result[0] + ", 나이 : " + result[1]);
			
			// 7. 여러 값 조회 - new 명령어로 조회
			List<Colleague> result9 = em.createQuery("select new com.projection.dto.Colleague(c.name, c.age) from colleague c", Colleague.class).getResultList();
			
			Colleague cdto = result9.get(0);
			System.out.println("7. 이름 : " + cdto.getName() + ", 나이 : " + cdto.getAge());
			
			
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
 	* 프로젝션[PROJECTION] - 투사, 투영
 	 - SELECT 절에 조회할 대상을 지정하는 것.
 	 - 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입) -> 관계형데이터베이스에서는 스칼라 타입만 들어갈 수 있다.
 	 - SELECT m FROM Member m -> 엔티티 프로젝션
 	 - SELECT m.team FROM Member m -> 엔티티 프로젝션
 	 - SELECT m.address FROM Member m -> 임베디드 타입 프로젝션
 	 - SELECT m.name, m.age FROM Member m -> 스칼라 타입 프로젝션
 	 - 참고 : DISTINCT로 중복 제거 
 	 
 	* 프로젝션 - 여러 값 조회
 	 (1) Query 타입으로 조회
 	 (2) Object[] 타입으로 조회
 	 (3) new 명령어로 조회
 	  - 단순 값을 DTO로 바로 조회
 	  ex) SELECT new com.projection.dto.Colleague(c.name, c.id) FROM Colleague c
 	  - 패키지 명을 포함한 전체 클래스 명 입력.
 	  - 순서와 타입이 일치하는 생성자 필요.
*/


