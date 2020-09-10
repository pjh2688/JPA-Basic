package com.cascadeANDorphanEntity.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "parent")
@SequenceGenerator(name = "parent_seq_generator", sequenceName = "parent_seq", allocationSize = 50)
public class Parent {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parent_seq_generator")
	@Column(name = "PARENT_ID")
	private Long id;
	
	@Column(name = "PARENT_NAME")	
	private String name;
	
	// 3. 다대일 양방향 걸어 주기. -> child에 연관관계 매핑이 되어있는 객체 이름을 mappedBy에 명시, 읽기만 가능하다.
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)  // parent가 persist 될 때 연관관계가 설정된 객체도 같이 persist 해주고 싶을 때 쓰는 옵션 -> cascade = CascadeType.ALL
	private List<Child> children = new ArrayList<Child>();                            // orphanRemoval = true -> 
	
	// 4. 연관 관계 편의 메소드 생성
	public void addChild(Child child) {
		children.add(child);
		child.setParent(this);
	}
	
	public Parent() {
		// TODO Auto-generated constructor stub
	}

	
	public List<Child> getChildren() {
		return children;
	}


	public void setChildren(List<Child> children) {
		this.children = children;
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
