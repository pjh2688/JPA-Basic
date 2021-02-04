package com.shopJpa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopJpa.domain.item.Book;
import com.shopJpa.domain.item.Item;
import com.shopJpa.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
	
	private final ItemRepository itemRepository;
	
	@Transactional  // 전체 ItemService를 readOnly true로 잡아줬지만 어노테이션은 가까운 곳을 우선으로 하기 때문에 읽기,쓰기 모두 가능하다.
	public void saveItem(Item item) {
		itemRepository.save(item);
	}
	
	public List<Item> findItems() {
		return itemRepository.findAll();
	}
	
	public Item findItem(Long itemId) {
		return itemRepository.findOne(itemId);
	}
	
	// 3. 변경 감지를 사용하기 위해선 service 계층에 @Transactional 어노테이션이 붙어 있어야 한다.
	@Transactional
	public void updateItem(Long itemId, String name, int price, int stockQuantity, String author, String isbn) {
		// 4. 우리는 일단 상품은 책만 있다고 가정
		Book findItem = (Book)itemRepository.findOne(itemId);  // itemId로 실제 DB에있는 영속상태의 데이터를 가져온다.
		
		findItem.setPrice(price);
		findItem.setName(name);
		findItem.setStockQuantity(stockQuantity);
		findItem.setAuthor(author);
		findItem.setIsbn(isbn);
		
		
		// 1. 스프링의 @Transactional 어노테이션에 의해 로직이 종료후 트랜잭션을 커밋시킨다.
		// 2. 커밋이 되면 JPA는 flush()라는 것을 호출한다. flush는 영속성 컨텍스트 안에 변경된 사항이 있는지 전부 확인하고 변경된 내용을 DB에 반영(update)해준다.
	}
}
