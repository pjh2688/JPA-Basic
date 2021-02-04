package com.jpaShop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpaShop.dao.ItemRepository;
import com.jpaShop.dto.Item;

import lombok.RequiredArgsConstructor;

@Service  // 1. service를 수행할 클래스라는 뜻.
@Transactional(readOnly = true)  // 2. 읽기 기능만 가능한 트랜잭션을 걸어준다.
@RequiredArgsConstructor  // 3. final이 붙은 변수만 생성자를 자동 생성해준다.
public class ItemService {

	private final ItemRepository itemRepository;  // 4. 
	
	@Transactional(readOnly = false)  // 6. 2번에서 전체 트랜잭션을 읽기 전용으로 설정해놨기 때문에 저장 부분은 쓰기 기능이므로 여기만 따로 false로 설정해준다.
	public void saveItem(Item item) {  // 5. 저장 service 
		itemRepository.save(item);
	}
	
	public List<Item> findItems() {
		return itemRepository.findAll();
	}
	
	public Item find(Long itemId) {
		return itemRepository.find(itemId);
	}
	
	// 변경 감지
	@Transactional(readOnly = false)  // 7. 쓰기 기능을 수행해야 하므로 전체가 읽기로 되어 있으니 따로 설정해준다. @Transactional 어노테이션은 default가 false
	public void updateItem(Long itemId, String name, int price, int stockQuantity) {  // 8. itemId, 찾을 물품 목록(item)
		
		Item findItem = itemRepository.find(itemId); // 9. 전달 받은 itemId로 실제 영속상태에 있는 item을 영속성 컨텍스트에서 꺼내온다.(영속 상태)
		
		findItem.setName(name);
		findItem.setPrice(price);
		findItem.setStockQuantity(stockQuantity);
	
	}
}
