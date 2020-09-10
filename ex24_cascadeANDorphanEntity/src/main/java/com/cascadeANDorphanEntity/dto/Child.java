package com.cascadeANDorphanEntity.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "child")
@SequenceGenerator(name = "child_seq_generator", sequenceName = "child_seq", allocationSize = 50)
public class Child {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "child_seq_generator")
	@Column(name = "CHILD_ID")
	private Long id;
	
	@Column(name = "CHILD_NAME")	
	private String name;
	
	@ManyToOne    // 1. 다대일에선 다쪽에 외래키가 들어가야 된다.
	@JoinColumn(name = "PARENT_ID")  // 2. 외래키 값을 parent_id로 설정.  
	private Parent parent;
	
	public Child() {
		// TODO Auto-generated constructor stub
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
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
