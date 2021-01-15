package com.join.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "division")
@SequenceGenerator(name = "division_seq_generator", sequenceName = "division_seq", allocationSize = 50)
public class Division {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "division_seq_generator")
	@Column(name = "DIVISION_ID")
	private Long id;
	
	@Column(name = "DIVISION_NAME")
	private String name;
	
	@OneToMany(mappedBy = "division")
	List<Colleague> colleagues = new ArrayList<Colleague>();

	public List<Colleague> getColleagues() {
		return colleagues;
	}

	public void setColleagues(List<Colleague> colleagues) {
		this.colleagues = colleagues;
	}

	public Division() {
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
