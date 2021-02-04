package com.shopJpa.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopJpa.domain.Member;
import com.shopJpa.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController  // 1. @Controller 기능과  @ResponseBody의 기능을 합쳐서 제공해주는 어노테이션
@RequiredArgsConstructor
public class MemberApiController {
	
	private final MemberService memberService;
	

	// 3. V1 : insert
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {  // 2. @RequestBody는 JSON으로 넘어온 데이터를 해당 매개변수 여기서는 member 매개변수에 Member 형태로 변환시켜서 바인딩시켜준다.
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	// 4. Member Response DTO(create)
	@Data  
	static class CreateMemberResponse {
		private Long id;

		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}
	
	// 5. V2 : insert -> 회원등록 API
	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
		
		Member member = new Member();
		member.setName(request.getName());
		
		Long id = memberService.join(member);
		
		return new CreateMemberResponse(id);
	}
	
	// 6. Member Request DTO(create)
	@Data
	static class CreateMemberRequest {
		@NotEmpty
		private String name;
	}
	
	// 7-1. V2 : update
	@PutMapping("/api/v2/members/{id}")  // 7-2. /api/v2/members/{id} ->  @PathVariable를 이용하기 위한 형태.
	public UpdateMemberResponse updateMemberV2(
				@PathVariable("id") Long id,  // 7-3. 7-2로 넘어오는 값(id 값)을 매개 변수(id)에 저장.
				@RequestBody @Valid UpdateMemberRequest request) {
		
		memberService.update(id, request.getName());
		
		Member findMember = memberService.findOne(id);  // 7-4. 그냥 PathVariable을 그냥 가져다 사용할 수도 있지만 변경된 데이터를 가지고 새로 엔티티를 조회하여 그 엔티티의 id값을 반환해 주는 것이 유지보수에 용이.
		
		return new UpdateMemberResponse(findMember.getId(), findMember.getName());
		
	}
	
	// 8. Member Request DTO(update)
	@Data
	static class UpdateMemberRequest {
		private String name;
	}	
	
	// 9. Member Response DTO(update)
	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}
	
	// 10. V1 : select(엔티티 객체가 전부 배열 형태로 넘어옴, 엔티티를 직접 노출) -> 권장하지 않는 방법, @JsonIgnore 어노테이션을 엔티티 객체에 붙여서 제외시키는 방법도 있지만 권장하지 않음. Array를 직접적으로 반환하기 때문에 반환시 확장이 불가능함(예를 들면 count같은 변수를 추가하고 싶은데 추가할 수가 없음 이구조는)
	@GetMapping("/api/v1/members")
	public List<Member> membersV1() {
		return memberService.findMembers();
	}
	
	// 11. V2 : 엔티티를 바로 반환하는 것이 아니라 껍데기(Result class)로 감싸서 변환.
	@GetMapping("/api/v2/members")
	public Result<List<MemberDto>> membersv2() {
		List<Member> findMembers = memberService.findMembers();
		
		/* findMembers를 Dto로 변환하는 방법 1
		int index = 0;
		for(Member member : findMembers) {
			new MemberDto(findMembers.get(index++).getName());
		}
		*/
		
		// findMembers를 Dto로 변환하는 방법 2 : java 8부터
		List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getName())).collect(Collectors.toList());
		
		return new Result<List<MemberDto>>(collect.size(), collect);
	}
	
	// 12. 
	@Data
	@AllArgsConstructor
	static class Result<T> {
		private int count;
		private T data;
	}
	
	// 13. DTO
	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String name;
	}
		
}
