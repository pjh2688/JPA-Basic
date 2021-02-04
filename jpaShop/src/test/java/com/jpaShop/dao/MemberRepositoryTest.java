package com.jpaShop.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jpaShop.dao.MemberRepository;
import com.jpaShop.dto.Member;

@RunWith(SpringRunner.class)  // JUNIT에게 테스트를 할거라고 알려주는 어노테이션
@SpringBootTest
@Rollback(false)  // 테스트 메소드에서도 롤백시키지 않고 커밋하고 싶을 때 사용(false).
public class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	
	@Test  // 주의 : @Transactional 어노테이션이 @Test 어노테이션이랑 같이 있으면 실행 종료 후 롤백을 시켜버린다.(데이터가 안들어감)
	@Transactional  // 주의 : JPA는 트랜잭션 안에서 돌아가야 한다.
	public void testMember() throws Exception {
		
		// given : 데이터 만들고
		Member member = new Member();
		member.setName("박종희");
		
		// when : 만든 데이터를 데이터베이스에 저장하고
		Long savedId =  memberRepository.save(member);
		Member findMember = memberRepository.find(savedId);
		
		// then : 그 데이터를 DB에서 찾아와서 만든 데이터랑 일치하는지 검증
		Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
		Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
		Assertions.assertThat(findMember).isEqualTo(member);
		
		System.out.println("findMember == member : " + (findMember == member));
	}
}

/*
  // 1.
  * war or jar로 추출하려면 gradle task 이용
  * cmd에서 실행 가능.
  * gradle로 실행할 때는 롤백이 되어 데이터가 안들어간다.
  * jUnit으로 실행할 때는 롤백이 false이기 때문에 데이터가 제대로 들어간다.
  
  // 2. 외부 라이브러리 사용 
   - https://github.com/gavlyukovskiy/spring-boot-data-source-decorator
 
 * */
