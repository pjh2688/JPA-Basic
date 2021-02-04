package com.jpaShop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", allocationSize = 50)
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
	@Column(name = "MEMBER_ID")
	private Long id;
	
	private String name;
	
	@Embedded
	private Address address;
	
	@OneToMany(mappedBy = "member") // 1. Order 읽기 전용(양방향 매핑)
	private List<Order> orders = new ArrayList<Order>();
	
}
