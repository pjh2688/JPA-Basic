package com.jpaShop06.dto;

import javax.persistence.Entity;

@Entity
public class Movie extends Item { // 상속 관계 매핑

	private String director;
	private String actor;
	
	public Movie() {
		// TODO Auto-generated constructor stub
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}
	
	
}
