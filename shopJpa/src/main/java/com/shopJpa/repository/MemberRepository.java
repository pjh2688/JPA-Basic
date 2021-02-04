package com.shopJpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.shopJpa.domain.Member;

import lombok.RequiredArgsConstructor;

@Repository  // 1. Component scan의 대상이 된다.
@RequiredArgsConstructor  // // 3. @RequiredArgsConstructor 어노테이션이 있으면 apprication.properties에 지정해놓은 영속성 컨텍스트 내용을 final이 붙은 곳에 @Autowired를 생략해도 생성자 주입해준다. 
public class MemberRepository {

	private final EntityManager em;  // 2. implementation 'org.springframework.boot:spring-boot-starter-data-jpa' 의존성에 의해서

	// 4. 저장
	public Long save(Member member) {
		em.persist(member);
		return member.getId();
	}
	
	// 5. 조회 1(single)
	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}
	
	// 6. 조회 2(multiple)
	public List<Member> findAll() {
		// jpql 사용
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}
	
	// 7. 조회 3(이름)
	public List<Member> findByName(String name) {
		// select m from Member m where m.name = :name -> : 파라미터 바인딩.
		return em.createQuery("select m from Member m where m.name = :name", Member.class)
				.setParameter("name", name)
				.getResultList();
	}
}

/*
 *카멜케이스[Camel Case]
  -> 낙타의 등이 중간에 불룩 튀어나온것처럼 글자의 중간중간이 불룩 튀어나와 있기 때문입니다.

 * 1) 하이버네이트로 구현된 기존 테이블 생성 규칙 : 엔티티의 필드명을 그대로 테이블 명으로 사용.
 * 2) 스프링부트에서 신규로 생긴 테이블 생성 규칙(
 *  - 카멜 케이스 -> 언더스코어(_) ex) memberPoint라는 엔티티는 -> member_point라는 테이블로)
 *  - 점(.) -> 언더스코어(_) ex) member.point -> member_point
 *  - 대문자 -> 소문자  
 * 
 */
