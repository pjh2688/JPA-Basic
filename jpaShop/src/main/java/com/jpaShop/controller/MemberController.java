package com.jpaShop.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jpaShop.dto.Address;
import com.jpaShop.dto.Member;
import com.jpaShop.form.MemberForm;
import com.jpaShop.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor  // 1. final이 붙은 변수만 생성자를 생성해준다.
public class MemberController {

		// 2. 1번에 의해 MemberService final이 붙은 곳에다가 생성자를 만들어 주고 주입해준다.
		private final MemberService memberService;
		
		// 3. memberForm 만들기, get방식으로 /members/new를 접근하면 여길 실행
		@GetMapping("/members/new")  // 5. 처음에 버튼을 클릭할때는 url을 타고오는 get방식이므로 getMapping
		public String createForm(Model model) {
			
			// 4. validation을 위해 사전에 검증 기능이 들어간 빈껍데기 폼을 만들어서 뷰(view)에 던져준다.
			model.addAttribute("memberForm", new MemberForm());
			return "members/createMemberForm";
		}
		
		// 5. post방식으로 /members/new로 접근하면 여길 실행
		@PostMapping("/members/new")  // 6. @Valid 어노테이션이 있으면 해당 객체에 사용된 validation에 설정된 어노테이션을 실행시킨다. 
		public String create(@Valid MemberForm form, BindingResult result) {  // 7. BindingResult 객체가 @Valid어노테이션이 쓰인 위치에 매개변수로 있으면 오류 내용이 넘어와 result변수에 저장된다..(Default 에러페이지로 안넘어가고)
			
			if(result.hasErrors()) {
				return "members/createMemberForm";
			}
			
			Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
			
			Member member = new Member();
			member.setName(form.getName());
			member.setAddress(address);
			
			memberService.join(member);
			
			return "redirect:/";
		}
		
		// 7.
		@GetMapping("/members")
		public String list(Model model) {
			List<Member> members = memberService.findMembers();
			model.addAttribute("members", members);
			return "members/memberList";
		}
}

/*
 * Tip : 
  => mapping 할때 '/'로 시작하면 절대경로가 되고, '/'로 시작하지 않으면 
        현재 url 주소를 기준으로 경로가 변동됩니다. 그래서 리소스(css,js)에는 대부분 절대경로를 사용합니다.
 */
