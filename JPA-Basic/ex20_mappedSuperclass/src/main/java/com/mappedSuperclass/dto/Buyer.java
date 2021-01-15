package com.mappedSuperclass.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "buyer")
@SequenceGenerator(name = "buyer_seq_generator", sequenceName = "buyer_seq", allocationSize = 50)
public class Buyer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buyer_seq_generator")
	@Column(name = "BUYER_ID")
	private Long id;
	
	@Column(name = "BUYER_NAME")
	private String name;
	
	public Buyer() {
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
