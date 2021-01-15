package com.join.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "colleague")
@SequenceGenerator(name = "colleague_seq_generator", sequenceName = "colleague_seq", allocationSize = 50)
public class Colleague {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colleague_seq_generator")
	@Column(name = "COLLEAGUE_ID")
	private Long id;
	
	@Column(name = "COLLEAGUE_NAME")
	private String name;
	
	@Column(name = "COLLEAGUE_AGE")
	private int age;
	
	@ManyToOne(fetch = FetchType.LAZY)  // 일대다 연관관계 맵핑에선 @ManyToOne의 fetch를 LAZY로 꼭 설정해줘야 한다.
	@JoinColumn(name = "DIVISION_ID")
	private Division division;

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Colleague() {
		// TODO Auto-generated constructor stub
	}
	
	public Colleague(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
}
