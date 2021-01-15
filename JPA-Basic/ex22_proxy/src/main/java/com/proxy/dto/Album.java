package com.proxy.dto;

import javax.persistence.Entity;

@Entity
public class Album extends Item {  // 상속 관계 매핑

	private String artist;
	private String etc;
	
	public Album() {
		// TODO Auto-generated constructor stub
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}
	
	
}
