package com.shopJpa.service.spring_data_jpa;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.Member;
import com.shopJpa.repository.spring_data_jpa.MemberJpaRepository;

import lombok.RequiredArgsConstructor;

@Service  // 1. component scan의 대상임을 명시
@Transactional(readOnly = true)  // 5. JPA는 트랜잭션 안에서 동작한다.
@RequiredArgsConstructor  // 6. final이 붙은 곳에다가 생성자를 통한 주입을 해준다.(lombok)
public class MemberJpaService {

	private final MemberJpaRepository memberJpaRepository;
	
	/*   
	 * 2-1. 회원 가입
	 */
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member);  // 중복 회원 검증
		memberJpaRepository.save(member);
		return member.getId();
	}
	
	// 2-2. 회원 가입 검증(validation) -> db에 member name 속성에 unique 제약조건을 걸어 값이 2개가 들어가는 걸 2중 방지해줘야 한다.
	private void validateDuplicateMember(Member member) {
		// EXCEPTION
		List<Member> findMembers = memberJpaRepository.findByName(member.getName());
		
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	/*   
	 * 3-1. 회원 전체 조회
	 */
	@Transactional(readOnly = true)  // 3-2. 전체 클래스에 트랜잭션을 걸어주고 @Transactional(readOnly = true) -> 이걸 설정해 놓으면 JPA가 성능을 최적화 해준다.
	public List<Member> findMembers() {
		return memberJpaRepository.findAll();
	}
	
	/*   
	 * 4-1. 회원 단건 조회
	 */
	@Transactional(readOnly = true) // 4-2. @Transactional(readOnly = true) -> 이걸 설정해 놓으면 JPA가 성능을 최적화 해준다.
	public Member findOne(Long memberId) {
		return memberJpaRepository.findById(memberId).get();
	}
	
	@Transactional
	public void update(Long memberId, String name) {  // 5. 회원 수정(이름 수정) 변경감지 사용
		Member member = memberJpaRepository.findById(memberId).get();
		member.setName(name);
	}
}
