package com.mappedSuperclass.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "seller")
@SequenceGenerator(name = "seller_seq_generator", sequenceName = "seller_seq", allocationSize = 50)
public class Seller extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seller_seq_generator")
	@Column(name = "SELLER_ID")
	private Long id;
	
	@Column(name = "SELLER_NAME")
	private String name;
	
	public Seller() {
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
