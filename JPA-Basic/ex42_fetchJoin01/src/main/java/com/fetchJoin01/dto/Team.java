package com.fetchJoin01.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;


@Entity(name = "team")
@SequenceGenerator(name = "team_seq_generator", sequenceName = "team_seq", allocationSize = 50)
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_seq_generator")
	@Column(name = "TEAM_ID")
	private Long id;
	
	@Column(name = "TEAM_NAME")
	private String name;
	
	// 양방향 걸어주기(컬렉션)
	@OneToMany(mappedBy = "team")
	List<Teammate> teammates = new ArrayList<Teammate>();
	
	public List<Teammate> getTeammates() {
		return teammates;
	}

	public void setTeammates(List<Teammate> teammates) {
		this.teammates = teammates;
	}

	public Team() {
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
	
	
}
