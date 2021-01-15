package com.bulkOperation.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "teammate")
@SequenceGenerator(name = "teammate_seq_generator", sequenceName = "teammate_seq", allocationSize = 50)
public class Teammate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teammate_seq_generator")
	@Column(name = "TEAMMATE_ID")
	private Long id;
	
	@Column(name = "TEAMMATE_NAME")
	private String name;
	
	@Column(name = "TEAMMATE_AGE")
	private int age;

//  객체 지향 모델링 X	
//	@Column(name = "TEAM_ID")
//	private Long teamId;
	
//  객체 지향 모델링 O
	@ManyToOne(fetch = FetchType.LAZY)  // N:1, 다대일은 지연로딩 설정을 꼭 해줘야 한다.
	@JoinColumn(name = "TEAM_ID")
	private Team team;
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Teammate() {
		// TODO Auto-generated constructor stub
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

//	public Long getTeamId() {
//		return teamId;
//	}
//
//	public void setTeamId(Long teamId) {
//		this.teamId = teamId;
//	}
//	
	
	
}
