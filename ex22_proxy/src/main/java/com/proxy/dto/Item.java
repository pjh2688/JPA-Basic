package com.proxy.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // 상속 관계 매핑
@DiscriminatorColumn
public abstract class Item extends BaseEntity {  // Item 클래스로 단독으로 객체를 생성할 일이 없기 때문에 추상 클래스로 만들어 준다.
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ITEM_ID")
	private Long id;
	
	private String name;
	private int price;
	private int stockQuantity;
	
	// 다대다 양방향 매핑(Category : Item)
	@ManyToMany(mappedBy = "items")  // 다대다 매핑 연관 관계의 주인(joinTable 설정을 해주는 곳이 주인)을 Category로 잡았기 때문에 mappedBy 해줘야 한다.
	private List<Category> categories = new ArrayList<Category>();
	
	public Item() {
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

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	
	
}
