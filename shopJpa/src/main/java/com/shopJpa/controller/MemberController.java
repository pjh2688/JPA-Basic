package com.shopJpa.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopJpa.domain.Address;
import com.shopJpa.domain.Member;
import com.shopJpa.form.MemberForm;
import com.shopJpa.service.MemberService;
import com.shopJpa.service.spring_data_jpa.MemberJpaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	
	private final MemberJpaService memberJpaService;
	
	@GetMapping("/members/new")
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberForm());  // validation 처리가 된 껍데기 화면 객체를 뷰에다가 넘겨준다.
		return "members/createMemberForm";
	}
	
	@PostMapping("/members/new")
	public String create(@Valid MemberForm form, BindingResult result) {  // @Valid 어노테이션이 붙은 매개변수 뒤에 BindingResult가 오면 @Valid에서 생긴 오류가 담긴다.
		
		if(result.hasErrors()) {
			return "members/createMemberForm";
		}
		
		Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
		
		Member member = new Member();
		member.setName(form.getName());
		member.setAddress(address);
		
//		memberService.join(member);
		
		// spring data jpa
		memberJpaService.join(member);
		
		return "redirect:/";
	}
	
	// 회원 목록 조회
	 @GetMapping("/members")
	 public String list(Model model) {
		 List<Member> members = memberService.findMembers();
		 
		 // spring data jpa
//		 List<Member> members = memberJpaService.findMembers();
		 
		 model.addAttribute("members", members);
		 
		 return "members/memberList";
	 }
}
