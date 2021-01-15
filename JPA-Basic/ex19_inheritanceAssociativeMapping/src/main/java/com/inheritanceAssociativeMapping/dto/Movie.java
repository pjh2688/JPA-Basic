package com.inheritanceAssociativeMapping.dto;

import javax.persistence.Entity;

@Entity(name = "moive")
public class Movie extends Goods {
	
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
