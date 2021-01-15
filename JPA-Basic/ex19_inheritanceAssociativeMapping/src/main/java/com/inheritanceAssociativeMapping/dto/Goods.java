package com.inheritanceAssociativeMapping.dto;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

@Entity(name = "goods")
@SequenceGenerator(name = "goods_seq_generator", sequenceName = "goods_seq", allocationSize = 50)
@Inheritance(strategy = InheritanceType.JOINED)  // 기본은 SINGLE_TABLE이다. -> SINGLE_TABLE 속성으로 생성한다면 @DiscriminatorColumn이 없어도 DTYPE속성이 생긴다. 
                                                 // TABLE_PER_CLASS 속성 : Goods 클래스를 상속하지 않고 각각 테이블에 만들어준다.
@DiscriminatorColumn  // 기본 값은 Entity 이름이다. 조인전략으로 테이블을 만들었을 때 사용했던 해당 테이블 명이 들어간다.
public abstract class Goods {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goods_seq_generator")
	@Column(name = "GOODS_ID")
	private Long id;
	
	@Column(name = "GOODS_NAME")
	private String name;
	
	@Column(name = "GOODS_PRICE")
	private int price;
	
	public Goods() {
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
}
