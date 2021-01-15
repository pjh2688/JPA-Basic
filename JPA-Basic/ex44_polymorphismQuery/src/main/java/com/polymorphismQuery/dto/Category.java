package com.polymorphismQuery.dto;

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

@Entity
public class Category extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)  // 하이버네이트 시퀀스 적용.
	@Column(name = "CATEGORY_ID")
	private Long id;
	
	@Column(name = "CATEGORY_NAME")
	private String name;
	
	// 셀프 양방향 매핑
	@ManyToOne(fetch = FetchType.LAZY)  // 부모 입장에선 하나의 부모에 여러 자식이므로 @OneToMany이므로 셀프로 할 때는 자식입장이니 @ManyToOne // ManyToOne은 Fetch 속성이 모두 eager가 기본 속성이기 때문에 Lazy(지연 로딩)로 바꿔야한다.
	@JoinColumn(name = "PARENT_ID")
	private Category parent;  // Category 타입의 부모 클래스 만들어주기.
	
	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<Category>();
	//
	
	@ManyToMany
	@JoinTable(  // 다대다 매핑시 중간 테이블을 만들기 위해 속성 설정
		name = "CATEGORY_ITEM", 
		joinColumns = @JoinColumn(name = "CATEGORY_ID"),
		inverseJoinColumns = @JoinColumn(name = "ITEM_ID")
	)
	private List<Item> items = new ArrayList<Item>();
	
	public Category() {
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

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChild() {
		return child;
	}

	public void setChild(List<Category> child) {
		this.child = child;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
