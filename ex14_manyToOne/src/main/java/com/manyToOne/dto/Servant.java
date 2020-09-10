package com.manyToOne.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "servant_seq_generator", sequenceName = "servant_seq", allocationSize = 50)
public class Servant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servant_seq_generator")
	@Column(name = "SERVANT_ID")
	private Long id;
	
	@Column(name = "SERVANT_NAME")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "MASTER_ID")
	private Master master;
	
	public Servant() {
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

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}
	
	
}
