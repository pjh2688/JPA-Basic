package com.jpaShop.dto;

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
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@SequenceGenerator(name = "category_seq_generator", sequenceName = "category_seq", allocationSize = 50)
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq_generator")
	@Column(name = "CATEGORY_ID")
	private Long id;
	
	private String name;
	
	@ManyToMany  // 1. 다대다 관계 매핑 작업(실무에선 사용 안함)
	@JoinTable( // 2. 다대다 관계 매핑을 위해 @JoinTable을 이용해 중간 테이블을 만들고
		name = "CATEGORY_ITEM",   // 3. 중간 테이블의 이름을 category_item으로 설정
		joinColumns = @JoinColumn(name = "CATEGORY_ID"),  // 4. 중간 테이블과 Category 객체와의 매핑을 위해 외래키 category_id를 만든다.
		inverseJoinColumns = @JoinColumn(name = "ITEM_ID")  // 5. 조인되는 반대편 테이블의 외래키 item_id를 만든다.
	)
	List<Item> items = new ArrayList<Item>();
	
	/* 셀프로 양방향 관계 걸어보기 */
	
	// 6. 계층 구조 만들기 1(부모)
	@ManyToOne(fetch = FetchType.LAZY)  // 7. Category 입장에선 부모는 1이므로 ManyToOne  // 12. 다대일 -> fetch = FetchType.LAZY
	@JoinColumn(name = "parent_id")  // 8. 부모에 접근할 수 있는 외래키를 parent_id라고 만든다.
	private Category parent;
	
	// 9. 계층 구조 만들기 2(자식)
	@OneToMany(mappedBy = "parent")  // 10. Category 입장에선 자식은 N이므로 OneToMany  // 11. mappedBy로 위에 parent와 연결해준다.  
	private List<Category> child = new ArrayList<Category>();
	
	// 13. 연관 관계 편의 메소드 -> 자기 자신과 양방향 매핑
	public void addCategory(Category category) {
		this.child.add(category);
		category.setParent(this);
	}
	
}
