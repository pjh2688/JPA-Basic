package com.jpaShop.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.jpaShop.dto.Item;

import lombok.RequiredArgsConstructor;

@Repository  // 1. Component scan의 대상임을 알려주는 어노테이션
@RequiredArgsConstructor // 2. final이 붙은 변수만 생성자를 자동 생성해준다. -> @Autowired 필요 없음
public class ItemRepository {

	private final EntityManager em;  // 3. 
	
	public void save(Item item) {  // 4. 저장
		if(item.getId() == null) {// 5. item 목록이 처음 저장될 때 새로운 item이라면 id값이 없으므로 null
			em.persist(item);  // 6. null때는 새로운 item이므로 영속성 컨엑스트에 저장.
		} else {
		//	em.merge(item);  // 7. id 값이 있다면 기존에 있는 item 목록이라는 말이므로 DB에 강제로 update한다는 뜻. (실무에선 절대 쓰지 말것).
		}
		
	}
	
	public Item find(Long id) {  // 8. 단건 조회
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll() {  // 9. 여러개 조회
		String jpql = "select i from Item i order by item_id desc";
		
		return em.createQuery(jpql, Item.class).getResultList();
	}
	
}
