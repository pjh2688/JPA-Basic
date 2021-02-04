package com.jpaShop.dao;

import java.util.List;

import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.jpaShop.dto.Member;

import lombok.RequiredArgsConstructor;

@Repository  // 1. Component scan의 대상임을 알려주는 어노테이션
@RequiredArgsConstructor // 14. MemberService에서 배운 @RequiredArgsConstructor 적용 
public class MemberRepository {  // 2. DAO라고 생각하면 된다.
	
	// 4. application.properties에 있는 설정들을 자동으로 주입해준다. 
	// 5. persistence.xml을 hello라는 이름으로 jpaMain 클래스에서 불러오던 것처럼 생각하면 된다.

//	@PersistenceContext  // 3. entity manager는 원래 @PersistenceContext로만 주입할 수 있었는데 스프링부트에서는 @Autowired도 주입 가능.
//	private EntityManager em;  
	
	// 15. MemberService에서 배운  @RequiredArgsConstructor에 의한 주입
	private final EntityManager em;
	
	// 6. INSERT
	public Long save(Member member) {
		em.persist(member);
		return member.getId();
	}
	
	// 7. SINGLE SELECT
	public Member find(Long id) {
		return em.find(Member.class, id);
	}
	
	// 8. TOTAL SELECT
	public List<Member> findAll() {
		String jpql = "select m from Member m order by member_id desc";  // 9. 엔티티 대상 jpql
		
		List<Member> result = em.createQuery(jpql, Member.class).getResultList();
		
		return result;
	}
	
	// 10. NAME SELECT
	public List<Member> findByName(String name) {
		String jpql = "select m from Member m where m.name = :name";  // 11. 파라미터 바인딩 사용
		
		List<Member> result = em.createQuery(jpql, Member.class)
						.setParameter("name", name)// 12. setParameter로 :name에다가 파라미터 name의 값을 집어 넣어서
						.getResultList();  // 13. list로 뽑아서 result에 저장.
		
		return result;
	}
	
	
}
