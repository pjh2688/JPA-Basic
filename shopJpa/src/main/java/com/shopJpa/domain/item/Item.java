package com.shopJpa.domain.item;

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

import com.shopJpa.domain.Category;
import com.shopJpa.exception.NotEnoughStockException;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // 상속을 시켰으니 상속 관계 전략을 설정해줘야 한다. -> SINGLE_TABLE
@DiscriminatorColumn(name = "dtype")  // DB 생성시 item들을 dtype으로 구분해준다.
public abstract class Item {  // item은 추상 클래스로 구현, 왜냐하면 다른 클래스에 상속을 시켜 구현시킬것이기 때문

	// 공통 속성
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;

	private String name;
	private int price;
	private int stockQuantity;
	
	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();
	
	/* 비즈니스 로직 */
	
	/**
	 *  stock 증가
	 */
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	/**
	 *  stock 감소
	 */
	public void removeStock(int quantity) {
		
		int restStock = this.stockQuantity - quantity;  // 현재 재고 수량에서 주문 수량을 빼면 남은 수량이 나온다.
	
		if(restStock < 0) {  // 남은 수량이 0보다 작으면
			throw new NotEnoughStockException("need more stock");
		}
		this.stockQuantity = restStock;
	}
	
}
