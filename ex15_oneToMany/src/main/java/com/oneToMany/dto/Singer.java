package com.oneToMany.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "singer")
@SequenceGenerator(name = "singer_seq_generator", sequenceName = "singer_seq", allocationSize = 50)
public class Singer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "singer_seq_generator")
	@Column(name = "SINGER_ID")
	private Long id;
	
	@Column(name = "SINGER_NAME")
	private String name;
	
	// 일대다(1:N)의 경우 1쪽을 연관관계의 주인으로 지정해야한다.
	// 일대다(1:N)의 경우 다쪽에 외래키가 설정되어 있지 않기 때문에 update쿼리를 한번 더 날라간다.(N쪽 테이블에 singer_id라는 외래키 컬럼을 만들어 준다.) -> 이래서 잘 안쓰임.
	@OneToMany
	@JoinColumn(name = "SINGER_ID")  // 일대다에서 조인컬럼을 설정안해주면 새로운조인테이블 하나가 더 생긴다.
	List<Song> songs = new ArrayList<Song>();
	
	public Singer() {
		// TODO Auto-generated constructor stub
	}
	

	public List<Song> getSongs() {
		return songs;
	}


	public void setSongs(List<Song> songs) {
		this.songs = songs;
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
