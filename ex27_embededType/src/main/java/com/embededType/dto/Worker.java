package com.embededType.dto;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "worker")
@SequenceGenerator(name = "worker_seq_generator", sequenceName = "worker_seq", allocationSize = 50)
public class Worker {
						
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_seq_generator")
	@Column(name = "WORKER_ID")
	private Long id;
	
	@Column(name = "WOKRER_NAME")	
	private String name;
	
	// 기간
	@Embedded
	private Period workPeriod;
	
	// 주소
	@Embedded
	private Address homeAddress;
	
	@Embedded
	@AttributeOverrides(value = { 
			@AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
			@AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
			@AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
	})
	private Address workAddress;
	
	public Worker() {
		// TODO Auto-generated constructor stub
	}

	public Period getWorkPeriod() {
		return workPeriod;
	}

	public void setWorkPeriod(Period workPeriod) {
		this.workPeriod = workPeriod;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
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
