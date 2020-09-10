package com.manyToOne.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "master")
@SequenceGenerator(name = "master_seq_generator", sequenceName = "master_seq", allocationSize = 50)
public class Master {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "master_seq_generator")
	@Column(name = "MASTER_ID")
	private Long id;
	
	@Column(name = "MASTER_NAME")
	private String name;
	
	// 다대일 양방향 걸어 주기.
	@OneToMany(mappedBy = "master")
	List<Servant> servants = new ArrayList<Servant>(); 
	
	public Master() {
		// TODO Auto-generated constructor stub
	}

	public List<Servant> getServants() {
		return servants;
	}


	public void setServants(List<Servant> servants) {
		this.servants = servants;
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
