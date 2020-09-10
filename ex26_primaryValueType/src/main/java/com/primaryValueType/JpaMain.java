package com.primaryValueType;

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
			
			
	
			em.flush();
			em.clear();

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
     * Identifier(식별자)란? 
      -> 자바에서 식별자는 클래스, 인터페이스,변수,메소드, 배열,문자열을 구분할수 있는 이름입니다. 이름은 다른것과 구분되어야 하기 때문에 서로 중복되지않은것을 사용해야 합니다.
                식별자는 길이의 제한이 없고 유니코드 문자를 사용하기 때문에 한글도 사용할수 있습니다. 대, 소문자를 구분하여 사용하고 몇가지 관례를 따르면 됩니다.
     
     *식별자 작성 규칙
	 (1) 특수문자(%,*,&,@,^등), 공백(탭,space등)은 식별자로 사용할수 없다.
	 (2) 한글을 사용할 수 있다.
	 (3) 자바 언어의 키워드는 사용 할 수 없다.
	 (4) 식별자의 첫번째 문자로 숫자로 사용할수 없다.
	 (5) true, false, null은 식별자로 사용할수 없다.
	 (6) 유니코드 문자를 사용한다.
	 (7) 공백이 없는 한 개의 단어로 구성되어야 한다.
	 (8) 문자, 숫자, '_', '$'를 사용 할 수 있다.
	 (9) 길이 제한이 없습니다.
     (10) 대,소문자를 구분한다.           

   * 기본값 타입 -> C에서 포인터 개념을 대입해서 생각하면 될거 같다.
    - 엔티티(Entity) 타입
     (1) @Entity로 정의하는 객체.
     (2) 데이터가 변해도 식별자로 지속해서 추적 가능.
      ex) 회원 엔티티의 키 값이나 나이 값을 변경해도 식별자로 인식 가능. 
    
    - 값 타입
     (1) int, Integer, String처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체.
     (2) 식별자가 없고 값만 있으므로 변경시 추적 불가.
      ex) 숫자 100을 200으로 변경하면 완전히 다른 값으로 대체된다.
      
    - 값 타입 분류
     (1) 기본값 타입 : 자바 기본 타입(int, double), wrapper 클래스(Integer, Long), String 등
      -> 생명주기를 엔티티(Entity)에 의존한다. 
        ex) 회원을 삭제하면 이름, 나이 필드도 함께 삭제된다.
      -> 값 타입은 공유하면 안된다.
        ex) 회원 이름 변경시 다른 회원의 이름도 함께 변경되면 안된다.(사이드 이펙트)
      
          참고 : 자바의 기본 타입은 절대로 공유되지 않는다.
      => int, double 같은 기본 타입(primitive type)은 절대 공유되지 않는다.
      => 기본 타입은 항상 값을 복사한다.
      => Integer 같은 래퍼(wrapper) 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이긴하지만 변경은 안된다. 
      
     (2) 임베디드 타입(embedded type, 복합 값 타입)
     
     (3) 컬렉션 값 타입(collection value type)
     
    *래퍼 클래스(Wrapper class)
	-> 프로그램에 따라 기본 타입의 데이터를 객체로 취급해야 하는 경우가 있습니다.
	    예를 들어, 메소드의 인수로 객체 타입만이 요구되면, 기본 타입의 데이터를 그대로 사용할 수는 없습니다.
	    이때에는 기본 타입의 데이터를 먼저 객체로 변환한 후 작업을 수행해야 합니다.
	    이렇게 8개의 기본 타입에 해당하는 데이터를 객체로 포장해 주는 클래스를 래퍼 클래스(Wrapper class)라고 합니다.
           래퍼 클래스는 각각의 타입에 해당하는 데이터를 인수로 전달받아, 해당 값을 가지는 객체로 만들어 줍니다.
	    이러한 래퍼 클래스는 모두 java.lang 패키지에 포함되어 제공됩니다.
    
*/


