package com.jpaShop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;   // 1. organize imports -> 테스트 케이스 생성 후 경고창 제거
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jpaShop.dao.MemberRepository;
import com.jpaShop.dto.Member;

@RunWith(SpringRunner.class)  // 2. JUNIT에게 테스트를 할거라고 알려주는 어노테이션 1
@SpringBootTest  // 3. JUNIT에게  테스트를 할거라고 알려주는 어노테이션 2 -> 2,3 두개가 있어야 springboot 프로젝트랑 integration 해서 테스트를 실행할 수 있다.
@Transactional  // 4. 트랜잭션 걸어주고 테스트가 끝나면 Rollback을 해준다. (테스트 패키지안에서)
public class MemberServiceTest {
	
	// 동일한 영속성 컨텍스트 
	@Autowired  // 5. 만들어진 MemberRepository 객체를 주입 해준다.
	MemberRepository memberRepository;  
	
	@Autowired  // 6. 만들어진 MemberService 객체를 주입 해준다.
	MemberService memberService;
	// 동일한 영속성 컨텍스트 
	
	@Autowired  // 13. 주입해주고
	EntityManager em;  // 12. 11번 작업을 하지 않아도 최종적으론 Rollback이지만 데이터베이스에 insert 쿼리를 날리는 것을 보고 싶다면 EntityManager를 생성 후
	
	@Test
	@Rollback(false)  // 11. @SpringBootTest어노테이션이 붙어 있는 곳의 루프를 돌면 마지막에 Rollback 시켜버리게끔 기본적으로 셋팅 되어 있다. 그래서 @Rollback(false)을 적어주어 롤백하지 말고 커밋하게끔 해주면 데이터가 정상적으로 들어간다.
	public void 회원가입() throws Exception {
		// given : 이런게 주어졌을 때
		Member member = new Member();  // 7. Member 객체를 만들고
		member.setName("박종희");  // 8. Member 객체의 이름을 set
		
		// when : 이런게 실행 되면
		Long saveId = memberService.join(member);  // 9. 생성한 member를 저장하고 id값 반환
		
		// then : 이런 결과가 나온다.
		em.flush();  // 14. flush를 해줘서 DB에 강제로 쿼리를 날려준다.(히지만 마지막에 롤백을 해버린다.)
		assertEquals(member, memberRepository.find(saveId));  // 10. MemberRepository에서 찾아 온 member랑 MemberService로 저장한 member랑 같은지 비교.
	}
	
	@Test(expected = IllegalStateException.class)  // 22. 19,20,21,22를 한 번에 처리하는 방법
	public void 회원가입_중복회원_예외처리() throws Exception {
		// given
		
		// 15. 똑같은 이름을 가진 member01, member02 생성
		Member member01 = new Member();
		member01.setName("박종희");
		
		Member member02 = new Member();
		member02.setName("박종희");
		
		// when
		memberService.join(member01);  // 18.
		memberService.join(member02);  // 23. 22번 추가해주고 이렇게 해주면 19,20,21,22를 한 번에 처리 
	/*
		try {  // 20. 19번을 해결하려면 try-catch로 감싸주고
			memberService.join(member02);  // 19. 18,19를 모두 join하면 같은 이름이기 때문에 IllegalStateException 예외메시지를 발생시킨다.
			
		} catch (IllegalStateException e) {  // 21. 해당 예외 IllegalStateException를 catch 한다음 
			// TODO: handle exception
			return;  // 22. return 시키게 감싸준다.
		}
	*/
		
		// 16. 예외가 발생해야 한다. 예외처리를 해줬기 때문에 예외 처리 메소드를 실행한 다음 예외가 발생했다는 값을 반환해준다.
		
		// then
		fail("예외가 발생해야 한다.");  // 17. 이것을 추가하면 무조건 fail을 발생시킨다. -> IllegalStateException 예외
	}

}
