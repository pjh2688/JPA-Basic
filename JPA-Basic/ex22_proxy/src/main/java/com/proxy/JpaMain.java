package com.proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Hibernate;

import com.proxy.dto.Member;

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
//			member.setName("박종희");
			
//			em.persist(member);
	
			Member member1 = new Member();
			member1.setName("박종희");
			em.persist(member1);
			
			Member member2 = new Member();
			member2.setName("박규란");
			em.persist(member2);
			
			em.flush();
			em.clear();
			
			// 영속성 컨텍스트 비우고
			
//			Member findMember = em.find(Member.class, member.getId());
//			Member findMember = em.getReference(Member.class, member.getId());
//			System.out.println("findMember = " + findMember.getClass());
//			System.out.println("findMember.id = " + findMember.getId());
//			System.out.println("findMember.name = " + findMember.getName());
			
			// 1. 비교
//			Member m1 = em.find(Member.class, member1.getId());
//			Member m2 = em.find(Member.class, member2.getId());  // true
			
//			Member m2 = em.getReference(Member.class, member2.getId());  // false -> type 비교시 프록시로 넘어오면 다른 타입의 객체로 인식한다.
			
//			System.out.println("m1 == m2 : " + (m1.getClass() == m2.getClass()));
//			System.out.println("m1 : " + (m1 instanceof Member));
//			System.out.println("m2 : " + (m2 instanceof Member));
			
			// 2. 영속성 컨텍스트에 찾으려는 엔티티가 이미 존재하면 em.getReference()를 호출해도 실제 엔티티를 반환해 준다.
//			Member m1 = em.find(Member.class, member1.getId());
//			System.out.println("m1 = " + m1.getClass());  // 원본을 반환.
			
//			Member ref = em.getReference(Member.class, m1.getId());
//			System.out.println("ref = " + ref.getClass());  // 프록시 객체를 반환하는 것이 아니라 원본을 반환.
			
			// 3. 영속성 컨테스트에 엔티티가 존재하지 않는데 em.getReference를 한 경우
//			Member ref1 = em.getReference(Member.class, member1.getId());
//			System.out.println("ref1 : " + ref1.getClass());
			
//			Member ref2 = em.getReference(Member.class, member1.getId());
//			System.out.println("ref2 : " + ref2.getClass());
			
//			System.out.println("ref1 == ref2 : " + (ref1 == ref2));
			
			// 4. em.getReference를 먼저하고 em.find를 그다음에 한 경우
//			Member ref = em.getReference(Member.class, member1.getId());
//			System.out.println("ref = " + ref.getClass());
			
//			Member member = em.find(Member.class, member1.getId());
//			System.out.println("member = " + member.getClass());
			
//			System.out.println("ref == member : " + (ref == member));
			
			// 5. 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제가 발생한다.
//			Member ref = em.getReference(Member.class, member1.getId());
//			System.out.println("ref = " + ref.getClass());  // 프록시 객체
			
			// 6.  영속성 컨텍스트에서 분리(try-catch부분에 예외처리 객체 e를 출력해줘야 오류메시지가 뜬다.)
//			em.detach(ref);  
//			em.clear();
			
//			ref.getName();
			
			// 7. 프록시 확인(프록시 객체가 초기화 되었나 여부 확인)
			Member ref = em.getReference(Member.class, member1.getId());
			System.out.println("ref = " + ref.getClass());  // 프록시 객체		
			
			// 프록시 초기화를 안해줬을 때 -> false
	//		System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(ref));
			 
			// 프록시 초기화를 해줬을 때 -> true
	//		ref.getName();
	//		System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(ref));
			
			// 8. 프록시 클래스 확인 방법.
	//		System.out.println("ref = " + ref.getClass());
			
			// 9. 프록시 강제 초기화
			Hibernate.initialize(ref);
			System.out.println("ref = " + ref.getClass());
			
			
			// 프록시로 먼저 조회하면 프록시 객체가 남아잇어서 em.find로 다시 조회해도 프록시 객체가 반환되고
			// em.find를 먼저 조회하면 그 다음에 em.getReference로 조회를해도 프록시 객체가 아닌 실제 객체가 반환된다는 게 핵심.
			
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
 	*프록시(proxy)
 	 1) 프록시 기초
 	  - em.find() vs em.getReference()
 	  (1) em.find() : 데이터 베이스를 통해서 실제 엔티티 객체를 조회.
 	  (2) em.getReference() : 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회.   
 	   ex) Client(고객)이 em.getReference()를 하면 Proxy라는 가짜 엔티티가 생성이 되고 그 안에는 Entity형 target 변수가 초기에 null 값을 가지고 있고 find를 할때 사용한 id값만 들고 있는 껍데기이다.
		
	 2) 프록시 특징
	  - 실제 클래스를 상속 받아서 만들어진다.
	  - 실제 클래스와 겉 모양이 같다.
	  - 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 된다.(이론상 그렇다)
	  - 프록시 객체는 실제 객체의 참조(target)를 보관하고 있다.
	  - 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드를 호출한다.
	   ex) Proxy 객체는 실제 객체의 생성된 모양과 거의 똑같지만 Entity 객체로 만들어진 참조 변수 target 변수를 가지고 있다. 그것을 통해 실제 객체의 메소드를 호출시킨다.
	  - 물론 처음에는 target 값이 NULL이기 때문에 JPA가 내부적으로 값이 NULL일 때는 영속성 컨텍스트에 초기화 요청(값을 요구)을 한다. 그리고 영속성 컨텍스트에서는 DB에서 조회해서 실제 엔티티 값을 가진 객체를 생성하고 그 객체의 주소값을 프록시 객체에 있는 target 변수와 연결시켜준다.  
	  - 프록시 객체는 처음 사용할 시점에 한 번만 초기화(값을 요청)한다.
	  - 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아니고 초기화되면 프록시 객체의 target 변수를 이용해서 실제 엔티티에 접근이 가능하다는 말이다.(포인터 같은 개념)
	  - 프록시 객체는 원본 엔티티를 상속 받기 떄문에 타입 체크시 주의해야한다.( == 비교가 아니라 instance of를 사용해야 한다. )	
	  - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 인티티를 반환시켜준다.(프록시 객체가 생성되지 않는다는 말)
	  - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제가 발생한다.
	   (하이버네이트에서는 org.hibernate.LazyInitializationException 예외를 발생시킨다.)
	   
	 3) 프록시 확인 
	  - 프록시 인스턴스의 초기화 여부 확인 -> PersistenceUnitUtil.isLoaded(Object entity)
	  - 프록시 클래스 확인 방법 -> entity.getClass().getName 출력하면 ..javasist.. or HibernateProxy... 이라고 나오면 프록시 객체이다.
	  - 프록시 강제 초기화 : org.hibernate.Hibernate.initialize(entity);
	  - 참고 : JPA 표준은 강제 초기화하지 않는 것이다. -> 강제 호출의 예 : member.getName();
*/


