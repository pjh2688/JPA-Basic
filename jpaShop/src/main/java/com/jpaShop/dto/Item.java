package com.jpaShop.dto;

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
import javax.persistence.SequenceGenerator;

import com.jpaShop.exception.NotEnoughStockException;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@SequenceGenerator(name = "item_seq_generator", sequenceName = "item_seq", allocationSize = 50)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // 3. 추상 클래스로 상속관계를 이용할 시 전략을 설정해줘야 한다.
@DiscriminatorColumn(name = "dtype")  // 4. 3번을 SINGLE_TABLE로 설정 했기 때문에 한 테이블에 모두 저장되서 구분해주기 위해 @DiscriminatorColumn(name = "dtype") 선언하고 Book,Music,Movie로 가서 @DiscriminatorValue를 지정해준다. 지정안해주면 클래스명으로 자동생성된다.
public abstract class Item {  // 1. 추상 클래스로 만든다.

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_generator")
	@Column(name = "ITEM_ID")
	private Long id;
	
	// 2. 공통 속성.
	private String name;
	private int price;
	private int stockQuantity;

	@ManyToMany(mappedBy = "items")  // Category와 양방향 매핑을 하고 읽기만 할 수 있게 mappedBy로 연결
	private List<Category> categories = new ArrayList<Category>();
	
	// 5. 비즈니스 로직
	
	// 5-1. 재고 수량 증가
	public void increaseStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	// 5-2. 재고 수량 감소
	public void decreaseStock(int quantity) {
		int resStock = this.stockQuantity - quantity;
		
		if(resStock < 0) {
			throw new NotEnoughStockException("need more stock");
		}
		
		this.stockQuantity = resStock;
	}
	
}

