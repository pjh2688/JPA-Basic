package com.jpaShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
  @SpringBootApplication 어노테이션이 있으면
   이 어노테이션이 붙어있는 패키지랑 패키지 하위에 있는 모든 것을
  component를 scan하고 spring bean으로 자동 등록 해준다. 
*/
@SpringBootApplication   
public class JpaShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaShopApplication.class, args);
		
	}

}
/*
 	*애플리케이션 아키텍쳐
 	
 	 Controller -> Service -> Repository(DAO) -> DB
 	 		↘		  ↓                          ↙
 	 			   Domain(DTO)
 	 			   
	 1) Controller : 웹 계층
	 2) Service : 비즈니스 로직, 트랜잭션 처리
	 3) Repository : JPA를 직접 사용하는 계층(entity manager), DB에 접근해서 데이터를 가져오는 계층(DAO)
	 4) Domain : 엔티티가 모여 있는 계층, 모든 계층에서 사용(DTO)
 	 	
 */
/*
 	* 예제를 단순화 하기 위해 다음 기능은 구현하지 않는다.
 	 1) 로그인과 권한 관리 X
 	 2) 파라미터 검증과 예외 처리 단순화
 	 3) 상품(item)은 도서(Book)만 사용
 	 4) 카테고리(Category)는 사용하지 않는다.
 	 5) 배송 정보도 사용하지 않는다.
 */
/*
 	* 엔티티 설계시 주의할 점
 	 1) 엔티티에는 가급적 Setter를 사용하지 말자.
 	  -> Setter가 모두 열려있다면 변경 포인터가 너무 많아서 유지보수가 어렵다.
 	  
 	 2) 모든 연관관계는 지연로딩으로 설정한다.
 	  - 즉시 로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생한다.
 	  - 실무에서는 모든 연관관계를 지연로딩[LAZY]로 설정해야 한다.
 	  - 연관된 엔티티를 함께 DB에서 조회해야할 때는 fetch join 이나 엔티티 그래프 기능을 사용한다.
 	  - @OneToOne, @ManyToOne 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 바꿔줘야 한다.
 	  
 	 *N+1 문제란?
 	  -> ORM에서 성능 이슈가 발생하는 가장 흔한 원인으로 N+1 Problem이 언급된다.
 	     N+1 Problem은 쿼리 1번으로 N건의 데이터를 가져 왔는데, 원하는 데이터를 얻기 위해
 	     N건의 데이터를 데이터 수 만큼 반복해서 2차적으로 쿼리를 수행하는 문제입니다 
 	        
 	  -> 쉽게 말해서 예를 들면 
 	  -> fetch 전략이 지연로딩[LAZY]로 되어 있지 않고 즉시로딩[EAGER]로 되어 있다면
 	  -> Order에 연관관계매핑이 되어있는 member를 가져오려고 할때 즉시 로딩이라면
 	  -> 일단 order에 관한 쿼리를 먼저 날리게 되는데 날려서 데이터를 가지고 와서 밑에를 보니 정작 가지고와야 할 데이터는
 	  -> 연관관계 매핑이 된 member 데이터를 가져오게되는 것이라 쿼리가 member를 조회하기 위해 N+1만큼 더 나가는 현상이다.
 	  
 	 3) 컬렉션은 필드에서 초기화하자.
 	  - 컬렉션은 필드에서 초기화 하는 것이 안전하다.
 	  - null 문제에서 안전하다.
 	  - 하이버네이트는 엔티티를 영속(persist) 시킬 때 하이버네이트가 컬렉션을 한 번 더 감싸서 하이버네이트에서 제공하는 내장 컬렉션으로 변경한다.
 	       그래서 되도록이면 컬렉션 자체를 set으로 수정하지도 말고 get으로 가져오더라도 함부로 가져오지 말 것. 이럴 경우 하이버네이트가 원하는 메커니즘이 동작 안할 수가 있다.
 
 	 4) 테이블, 컬럼명 생성 전략
 	  - 스프링 부트에서는 하이버네이트 기본 매핑 전략을 변경해서 실제 테이블 필드명은 다르다.
 	  - 하이버네이트는 엔티티의 필드명을 그대로 테이블 명으로 사용한다.
 	  - 스프링 부트에서 작업할 경 우 SpringPhysicalNamingStrategy를 사용하여 바꿔준다.
 	   ex) Order에 orderDate 속성은 데이터베이스에가서 테이블 필드를 생성할때 order_date라는 이름으로 바뀌어 생성 된다.
 	  
 	  - application.properties에서 설정하는데 
 	   (1) 논리적 생성 : 명시적으로 컬럼이나 테이블명을 직접 적어주지 않으면  spring.jpa.hibernate.naming.implicit-strategy 사용
	   (2) 물리명 적용 : 테이블 모든 곳의 컬럼명에 적용 spring.jpa.hibernate.naming.physical-strategy
 */
 