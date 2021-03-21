package com.bootapp.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Board {

// 참고 : ALTER SEQUENCE "시퀀스 이름" RESTART WITH 1; -> 시퀀스 초기화하는 방법
// ALTER SEQUENCE board_b_no_seq restart WITH 1; 
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) // strategy=GenerationType.IDENTITY 시퀀스를 자기만의 시퀀스를 사용한다는 말.
	private long id;

	private String title;

	private String content;

	private String writer;

	private LocalDate date;

	private int hit;

	public Board() {
		// TODO Auto-generated constructor stub
		this.setDate(LocalDate.now());
	}
}