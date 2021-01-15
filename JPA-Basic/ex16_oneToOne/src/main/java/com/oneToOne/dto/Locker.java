package com.oneToOne.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "locker")
@SequenceGenerator(name = "locker_seq_generator", sequenceName = "locker_seq", allocationSize = 50)
public class Locker {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locker_seq_generator")
	@Column(name = "LOCKER_ID")
	private Long id;
	
	@Column(name = "LOCKER_NAME")
	private String name;
	
	@OneToOne(mappedBy = "locker")
	private Guest guest;  // 읽기 전용
	
	public Locker() {
		// TODO Auto-generated constructor stub
	}

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
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
