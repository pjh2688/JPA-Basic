package com.jpaShop.service;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jpaShop.dto.item.Book;

@RunWith(SpringRunner.class)  // 1. JUNIT에게 테스트를 할거라고 알려주는 어노테이션 1
@SpringBootTest  // 2. JUNIT에게  테스트를 할거라고 알려주는 어노테이션 2 -> 1,2 두개가 있어야 springboot 프로젝트랑 integration 해서 테스트를 실행할 수 있다.
public class ItemUpdateTest {
	
	@Autowired
	EntityManager em;
	
	// 변경 감지 == dirty checking
	@Test
	public void updateTest() throws Exception {
		
		Book book = em.find(Book.class, 1L);
		
		// transaction
		book.setName("이름바꾸기");
		
		// transaction commit;
		
		
	}

}
