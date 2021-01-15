package com.oneToOne.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "guest")
@SequenceGenerator(name = "guest_seq_generator", sequenceName = "guest_seq", allocationSize = 50)
public class Guest {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guest_seq_generator")
	@Column(name = "GUEST_ID")
	private Long id;
	
	@Column(name = "GUEST_NAME")
	private String name;
	
	@OneToOne
	@JoinColumn(name = "LOCKER_ID")
	private Locker locker;
	
	public Guest() {
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
