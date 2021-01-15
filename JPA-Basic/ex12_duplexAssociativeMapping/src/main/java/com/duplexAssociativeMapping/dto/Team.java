package com.duplexAssociativeMapping.dto;

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
	
	private String name;
	
	/*
	  *객체와 테이블이 관계를 맺을 떄 차이점.
	   1. 두 객체 사이의 연관관계(team,teammate) -> 참조 값이 필요하다는 말.
	    - team -> teammate : 1개(단방향)
	    - team <- teammate : 1개(단방향)
	   
	   2. 테이블 사이의 연관관계(team, teammate)
	    - team <-> teammate : 1개(양방향), 외래키 이용하여 조인하면 서로의 정보를 알 수 있다.
	    
	   3. 두 객체 사이의 양방향 관계는 실제로 양방향 관계가 아니라 서로 다른 단방향 관계 2개이다.
	   
	   4. 두 객체를 양방향으로 참조하게 하려면 단방향 연관 관계를 2개 만들어야 한다. 
	   
	   5. 그리고 두 객체 중 하나의 키만으로 외래키를 관리해서 사용해야 한다.
	   
	   6. 연관 관계의 주인(Owner) 설정
	    - 양방향 매핑 규칙
	     (1) 객체의 두 관계중 하나를 연관관계의 주인으로 지정.
	     (2) 연관관계의 주인(Owner)만이 외래 키를 관리(등록, 수정)
	     (3) 주인(Owner)이 아닌쪽은 읽기만 가능.
	     (4) 주인(Owner)는 mappedBy 속성 사용 X
	     (5) 주인이 아니면 mappedBy 속성으로 주인 지정.
	     (6) 외래 키가 지정되어 있는 테이블을 주인(Owner)로 정해라.(ManyToOne 에서 Many쪽)
	  
	*/
	
	@OneToMany(mappedBy = "team") // 주인(Owner)을 team객체로 지정.
	private List<Teammate> teammates = new ArrayList<>();

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
	

	public List<Teammate> getTeammates() {
		return teammates;
	}

	public void setTeammates(List<Teammate> teammates) {
		this.teammates = teammates;
		
	}
}
