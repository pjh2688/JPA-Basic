package com.bootapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "employees")
public class Employee {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) // strategy=GenerationType.IDENTITY 시퀀스를 자기만의 시퀀스를 사용한다는 말.
	private long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	private String email;

}
