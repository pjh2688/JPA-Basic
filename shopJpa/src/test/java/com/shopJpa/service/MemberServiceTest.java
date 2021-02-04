package com.shopJpa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.Member;
import com.shopJpa.repository.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	EntityManager em;

	@Test
	@Rollback(false)
	public void 회원가입() throws Exception {
		// given
		Member member = new Member();
		member.setName("park");
		
		// when
		Long savedId = memberService.join(member);
		
		// then
//		em.flush();  // 강제로 위에 변경 or 수정 내역을 영속성 컨텍스트에서 db에 쿼리를 날려주는 메소드.
		assertEquals(member, memberRepository.findOne(savedId));
	}
	
	@Test(expected = IllegalStateException.class)
	public void 중복_회원_예외() throws Exception {
		// given
		Member member1 = new Member();
		member1.setName("kim1");
		
		Member member2 = new Member();
		member2.setName("kim1");
		
		// when
		memberService.join(member1);
		memberService.join(member2);
			// -> 예외가 발생해야 한다.
		
		// then
		fail("예외가 발생해야 한다.");
	}
	
	
	
}
