package com.jpaShop.service;

import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpaShop.dao.MemberRepository;
import com.jpaShop.dto.Member;

// import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)  // 8. 성능 최적화(전체 적용)
// @AllArgsConstructor // 11. 생성자를 따로 생성안해도 만들어준다.
@RequiredArgsConstructor  // 12. 11번 어노테이션보다 업그레이드 된 버전으로 final이 붙은 변수만 생성자를 생성해준다.
public class MemberService {

	// 1-1. @Autowired injection : 만들어진 MemberRepository를 주입한다.
//  @Autowired  
//	private MemberRepository memberRepository;
	
	// 1-2. setter injection(단점 : 중간에 바꿀 수가 있음)
//  @Autowired  
//	private MemberRepository memberRepository;
	
//	public void setMemberRepository(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	}
	
	// 1-3. 생성자 injection 1
//	private MemberRepository memberRepository;	
	
//	@Autowired
//	public MemberService(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	}
	
	// 1-4. 생성자 injection 2 :  최선버전 스프링에서는 @Autowired가 없어도 생성자가 하나뿐이라면 자동으로 주입해준다.
//	private MemberRepository memberRepository;
	
//	public MemberService(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	
//	}
	
	// 1-5. final 
//	private final MemberRepository memberRepository;
	
//	public MemberService(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	
//	}
	
	// 1-6. lombok 이용 1 -> @AllArgsConstructor : 생성자 생략 가능.
//	private MemberRepository memberRepository;
	
	// 1-7. lombok 이용 2 -> @RequiredArgsConstructor : final이 붙은 곳에만 생성자를 만들어 주입해준다. 앞으로 이 스타일로 적용.(final이 붙으면 생성자가 없으면 오류를 표시해줌.(// @AllArgsConstructor or @RequiredArgsConstructor)
	private final MemberRepository memberRepository;
	
	// 2. 회원 가입
	@Transactional(readOnly = false)  // 9. join은 insert,select 둘다 해야 되기 때문에 false로 따로 설정(원래 false가 기본)  // 10. @Transactional를 MemberService 전체에 걸어줘도 내부에 @Transactional을 한 번 더 사용하면 이곳을 우선 실행한다.
	public Long join(Member member) {
		validateDuplicateMember(member);  // 4. 중복 회원 검증
		
		memberRepository.save(member);  // 5. 회원 저장(가입)

		return member.getId();
	}
	
	// 3. 회원 가입 검증(중복 확인)
	private void validateDuplicateMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName());
		
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
		
	}
	
	// 6. 회원 전체 조회
	public List<Member> findMembers() {
		List<Member> findAllMembers = memberRepository.findAll();
		return findAllMembers;
	}
	
	// 7. 단일 회원 조회
	public Member findSingleMember(Long id) {
		Member member = memberRepository.find(id);
		
		return member;
	}
}
