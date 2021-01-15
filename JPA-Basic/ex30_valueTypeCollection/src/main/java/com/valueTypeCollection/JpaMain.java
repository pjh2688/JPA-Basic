package com.valueTypeCollection;

// import java.util.List;
// import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.valueTypeCollection.dto.Address;
import com.valueTypeCollection.dto.AddressEntity;
import com.valueTypeCollection.dto.Personal;

public class JpaMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  // 1. 문제 없이 실행이 되었다면(엔티티 매니저 팩토리는 하나만 생성하고 App 전체에서 공유한다.)
		
		EntityManager em = emf.createEntityManager();  // 2. entityManager를  EntityManagerFactory에서 꺼내온다(쉽게 생각해서 DB의 connection 객체를 꺼내왔다고 보면 된다.). -> 엔티티 매니저는 서로 다른 쓰레드간에 공유해서 사용하면 안된다 특정 쓰레드에서 사용했다면 사용하고 버려야 한다.
		
		EntityTransaction tx = em.getTransaction();  // 3. 2번에서 얻어온 DB connection 객체에서 트랜잭션 하나를 얻어 온다.
		// 여기는 데이터 처리하는 곳(시작)
		tx.begin();  // 4. 트랜잭션 시작. -> jpa는 트랜잭션 안에서 작업을 해야 제대로 작동한다 트랜잭션으로 안 묶어주면 안된다(핵심).
		try {  			
			
			Personal psn = new Personal();
			psn.setName("박종희");
			psn.setHomeAddress(new Address("충청북도 청주시", "상당구 산성로 55", "28720"));
			
			psn.getFavoriteFoods().add("치킨");
			psn.getFavoriteFoods().add("족발");
			psn.getFavoriteFoods().add("피자");
			
//			psn.getAddressHistory().add(new Address("충청북도 청주시", "상당구 산성로 55", "탑동 LH 아파트 104-1602"));
//			psn.getAddressHistory().add(new Address("충청남도 천안시 동남구 병천면", "충절로 1600", "한국기술교육대학교"));
			
			// 값 타입 컬렉션을 Entity로 승급시키기
			psn.getAddressHistory().add(new AddressEntity("충청북도 청주시", "상당구 산성로 55", "탑동 LH 아파트 104-1602"));
			psn.getAddressHistory().add(new AddressEntity("충청남도 천안시 동남구 병천면", "충절로 1600", "한국기술교육대학교"));
			
			em.persist(psn);
			
			// 영속성 컨텍스트 초기화
			em.flush();
			em.clear();
			
		//  값 타입 컬렉션 테스트	
//			System.out.println("=============== START ===============");
//			Personal findPsn = em.find(Personal.class, psn.getId());
//			
//			// 컬렉션들은 기본적으로 지연로딩이다.(사용하는 시점에 쿼리가 나간다는 뜻)
//			List<Address> addressHistory = findPsn.getAddressHistory();
//			for(Address address : addressHistory) {
//				System.out.println("city = " + address.getCity());
//				System.out.println("street = " + address.getStreet());
//				System.out.println("zipcode = " + address.getZipcode());
//			}
//			
//			Set<String> favoriteFoods = findPsn.getFavoriteFoods();
//			for(String favoritFood : favoriteFoods) {
//				System.out.println("favoriteFood = " + favoritFood);
//			}
//			
//			System.out.println("=============== END ===============");
		
		//  값 타입 컬렉션 수정 
			System.out.println("=============== START ===============");
			Personal findPsn = em.find(Personal.class, psn.getId());
			
		//  잘못된 수정 방법 -> 값 하나만 수정하면 안됨.
		//	findPsn.getHomeAddress().setCity("수정");
			
		//  올바른 수정 방법 -> 주소를 통채로 바꿔줘야한다. 다시 말하면 새로운 인스턴스로 아예 교체를 해줘야한다는 말(new).
//			System.out.println("변경되기 전 homeAddress city : " + findPsn.getHomeAddress().getCity());
//			System.out.println("변경되기 전 homeAddress street : " + findPsn.getHomeAddress().getStreet());
//			System.out.println("변경되기 전 homeAddress zipcode : " + findPsn.getHomeAddress().getZipcode());
//			
//			Address a = findPsn.getHomeAddress();
//			findPsn.setHomeAddress(new Address("Cheongju, North Chungcheong Province", a.getStreet(), a.getZipcode()));
//			
//			System.out.println("변경된 후 homeAddress city : " + findPsn.getHomeAddress().getCity());
//			System.out.println("변경된 후 homeAddress street : " + findPsn.getHomeAddress().getStreet());
//			System.out.println("변경된 후 homeAddress zipcode : " + findPsn.getHomeAddress().getZipcode());
//			
//			System.out.println("=============== END ===============");
			
		//  치킨 -> 한식 : 해당 값 타입 데이터를 지우고 다시 add
			findPsn.getFavoriteFoods().remove("치킨");
			findPsn.getFavoriteFoods().add("한식");
			
		//  주소 변경
//			findPsn.getAddressHistory().remove(new Address("충청북도 청주시", "상당구 산성로 55", "28720"));  // 컬렉션 객체를 다룰때는 equals 메소드를 꼭 재정의 해줘야 한다.
//			findPsn.getAddressHistory().add(new Address("Cheongju, North Chungcheong Province", "55, Sanseong-ro, Sangdang-gu", "28720"));
			
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
   * 값 타입 컬렉션
    - 값 타입을 하나 이상 저장할 때 사용.
    - @ElementCollection, @CollectionTable 어노테이션 사용.
    - 데이터베이스는 컬렉션을 같은 테이블 안에 저장할 수 없다.
    - 컬렉션 데이터를 데이터베이스에 저장하기 위한 별도의 테이블이 필요하다는 말이다.
    
   * 값 타입 컬렉션 특징
    - 값 타입 컬렉션도 지연 로딩 전략을 사용한다.
    - 참고 : 값 타입 컬렉션은 영속성 전이(Cascade) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다.
    
   * 값 타입 컬렉션의 제약사항
    - 값 타입은 엔티티와 다르게 식별자 개념이 없다.
    - 값은 변경하면 추적이 어렵다.
    - 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 기존에 있던 모든 데이터를 삭제하고, 값 타입 컬렉션에 변경된 데이터를 새로 다시 저장한다.
	- 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야 한다. -> null값 X, 중복 저장 X
	-> 그래서 실무에선 잘 안쓴다.
	
   * 값 타입 컬렉션의 대안
    - 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계 사용을 고려한다.
    - 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용한다. ex) AddressEntity
    
   * 정리
    - 엔티티 타입의 특징 
     1) 식별자가 있다.
     2) 생명 주기가 관리된다.
     3) 공유할 수 있다.
     
    - 값 타입의 특징
     1) 식별자가 없다.
     2) 생명주기를 다른 엔티티에 의존한다.
     3) 공유하지 않는 것이 안전하지만 굳이 사용하려면 복사해서 사용해야 한다.
     4) 3)번의 이유로 사용할 때는 불변 객체로 만드는 것이 안전하다.
     
    - 값 타입 사용시 주의 사항
     1) 값 타입은 정말 값 타입이라 판단될 때만 사용.
     2) 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안된다.
     3) 식별자가 필요하고, 지속해서 값을 추적하고 변경해야 한다면 그것은 값 타입으로 사용하면 안되고 엔티티로 사용해야한다.
     
*/    


