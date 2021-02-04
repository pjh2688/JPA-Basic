package com.shopJpa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.shopJpa.domain.item.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;
	
	private String name;
	
	@ManyToMany  
	@JoinTable(name = "category_item", // 다대다는 join table이 필요.
			   joinColumns = @JoinColumn(name = "category_id"),  // 외래키를 category_id 와
			   inverseJoinColumns = @JoinColumn(name = "item_id"))  // item_id 두개 생성
	private List<Item> items = new ArrayList<>();
	
	// 게층 구조 설정(연관 관계 매핑)
	@ManyToOne(fetch = FetchType.LAZY)  // 부모입장에선 부모 1명에 자식 여러명
	@JoinColumn(name = "parent_id")  // 외래키를 parent_id로 생성
	private Category parent;
	
	@OneToMany(mappedBy = "parent")  // 자식입장에선 자식 여러명이 부모 1명
	private List<Category> child = new ArrayList<>();
	
	// 양방향일때 사용 //
	
	/* 연관 관계 편의 메소드(1:N) ->1:N은 add */
	public void addChildCategory(Category child) {
		// 부모 컬렉션에 자식을 추가 해주고
		this.child.add(child);
		
		// 반대로 자식 컬렉션에도 부모를 추가
		child.setParent(this);
	}
	
	
}
