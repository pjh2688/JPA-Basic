package com.inheritanceAssociativeMapping.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "album")
@DiscriminatorValue("Album")  // DTYPE값을 바꿔주는 어노테이션이다. 기본 값이 클래스 명이다.
public class Album extends Goods {
	
	private String artist;
	
	public Album() {
		// TODO Auto-generated constructor stub
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	
}
