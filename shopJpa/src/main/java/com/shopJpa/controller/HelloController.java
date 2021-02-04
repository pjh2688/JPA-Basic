package com.shopJpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopJpa.domain.Member;
import com.shopJpa.service.MemberService;

@Controller
public class HelloController {

	@Autowired
	MemberService ms;
	
	@GetMapping("hello")
	public String hello(Model model) {
		model.addAttribute("data", "hello");
		
		Member member = new Member();
		
		System.out.println(member.getOrders().getClass()); // class java.util.ArrayList
	
		ms.join(member);
		System.out.println(member.getOrders().getClass()); // class org.hibernate.collection.internal.PersistentBag
		
		// DB 연동 테스트
		return "hello";
	}
	
}
/*
 	*예제를 단순화 하기 위해 다음 기능은 구현 X
     - 로그인과 권한 관리 X
     - 파라미터 검증과 예외 처리 단순화
     - 상품은 도서(Book)만 사용
     - 카테고리(Category)는 사용하지 않는다.
     - 배송 정보(Delivery)도 사용하지 않는다. 
 */
