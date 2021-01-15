package com.oneToMany.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "song")
@SequenceGenerator(name = "song_seq_generator", sequenceName = "song_seq", allocationSize = 50)
public class Song {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "song_seq_generator")
	@Column(name = "SONG_ID")
	private Long id;
	
	@Column(name = "SONG_NAME")
	private String name;
	
//  일대다 양방향 : 이런 매핑은 공식적으로 존재하진 않는다. 읽기 전용 필드를 사용해서 양방향처럼 사용하는 방법이다.
//	@ManyToOne
//	@JoinColumn(name = "SINGER_ID", insertable = false, updatable = false)
//	private Singer singer;
	
	public Song() {
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
