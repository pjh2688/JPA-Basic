package com.shopJpa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter  // 실무에서는 setter를 닫아 놓자.
public class Member {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)  // strategy=GenerationType.IDENTITY 시퀀스를 자기만의 시퀀스를 사용한다는 말.
	@Column(name = "member_id")
	private Long id;
	
	@NotEmpty  // javax.validation에서 제공 : 이름은 필수., 매개변수 파라미터 앞에 @Valid 어노테이션이 붙으면 동작
	private String name;
	
	@Embedded  // 내장 타입임을 선언.
	private Address address;
	
	@JsonIgnore  // 양방향 연관관계에서는 한쪽을 @JsonIgnore 꼭 달아줘야 한다.
	@OneToMany(mappedBy = "member")  // Order 테이블에 있는 member 필드를 지정.(읽기 전용)
	private List<Order> orders = new ArrayList<>();
}
// tip : 두 객체 연관관계 중 하나를 정해서 테이블의 외래키를 관리해야 하는데 이것을 연관관계의 주인이라고 한다.
