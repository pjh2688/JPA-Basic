package com.shopJpa.dao;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.Member;
import com.shopJpa.repository.MemberRepository;

@RunWith(SpringRunner.class) // 2. junit한테 스프링부트 관련 클래스를 test할꺼라고 알려준다.
@SpringBootTest  // 1. 
public class MemberRepositoryTest {

	@Autowired  // 4.
	MemberRepository memberRepository;  // 3.
	
	@Test  // 6. 
	@Transactional  // 7. JPA는 트랜잭션 안에서 실행되어야 한다. 그리고 @Test 어노테이션 안에 @Transactional 어노테이션이 있으면 데이터를 마지막에 롤백시킨다.
	@Rollback(false)  // 8. 롤백을 안한다고 설정하면 데이터가 정상적으로 insert 된다.
	public void testMember() throws Exception {  // 5.
		// given
		Member member = new Member();
		member.setName("memberA");
		
		// when
		Long savedId = memberRepository.save(member);
		Member findMember = memberRepository.findOne(savedId);
		
		// then
		Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
		Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
		Assertions.assertThat(findMember).isEqualTo(member);
		
		System.out.println("findMember == member " + (findMember == member));
	}
}
