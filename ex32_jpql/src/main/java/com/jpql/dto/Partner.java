package com.jpql.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "partner")
@SequenceGenerator(name = "partner_seq_generator", sequenceName = "partner_seq", allocationSize = 50)
public class Partner {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partner_seq_generator")
	@Column(name = "PARTNER_ID")
	private Long id;
	
	@Column(name = "PARTNER_NAME")
	private String name;
	
	public Partner() {
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
