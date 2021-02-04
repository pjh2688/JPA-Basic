package com.jpaShop.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.jpaShop.dto.Order;
import com.jpaShop.dto.OrderSearch;

import lombok.RequiredArgsConstructor;

@Repository  // 1. Component scan의 대상임을 알려주는 어노테이션
@RequiredArgsConstructor  // 2. 
public class OrderRepository {

	private final EntityManager em;  // 3. 2번 어노테이션을 이용한 주입.
	
	// 4. INSERT
	public void save(Order order) {
		em.persist(order);
	}
	
	// 5. SINGLE SELECT
	public Order find(Long id) {
		return em.find(Order.class, id);
	}
	
	// 6. 조회 1
	public List<Order> findAllByString(OrderSearch orderSearch) {
    /*  6-1. 기본 : Order o와 o와 연관된 member m과 join 하라는 뜻
	    String jpql = "select o from Order o join o.member m";
	
		return null;
	*/
		
	/*  6-2. 파라미터 바인딩 1
		String jpql = "select o from Order o join o.member m" + 
					  " where o.status = :status" + 
					  " and m.name like :name";
		
		return em.createQuery(jpql, Order.class)
				.setParameter("status", orderSearch.getOrderStatus())
				.setParameter("name", orderSearch.getMemberName())
				.getResultList();
	*/
		
	/*  6-3. 파라미터 바인딩 2 -> 검색 결과를 제한하고 싶을 때(여기선 1000개로 제한)
		String jpql = "select o from Order o join o.member m" + 
				  " where o.status = :status" + 
				  " and m.name like :name";
	
		return em.createQuery(jpql, Order.class)
				.setParameter("status", orderSearch.getOrderStatus())
				.setParameter("name", orderSearch.getMemberName())
				.setMaxResults(1000)
				.getResultList();
	*/
		
	/*  6-4. 파라미터 바인딩 3 -> 페이징시 시작 페이지를 설정한 후 그 시작페이지(100p)부터 검색 결과를 제한(1000p)해서 가져오고 싶을 때 
		String jpql = "select o from Order o join o.member m" + 
				  " where o.status = :status" + 
				  " and m.name like :name";
	
		return em.createQuery(jpql, Order.class)
				.setParameter("status", orderSearch.getOrderStatus())
				.setParameter("name", orderSearch.getMemberName())
				.setFirstResult(100)
				.setMaxResults(1000)
				.getResultList();
	*/
		
	/*  6-5. 동적 쿼리 1 -> 비효율적인 방법 */		
		String jpql = "select o from Order o join o.member m";
		
		boolean isFirstCondition = true;
		
		// 1) 주문 상태 검색
		if(orderSearch.getOrderStatus() != null) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			
			jpql += " o.status = :status";
		}
		
		// 2) 회원 이름 검색
		if(StringUtils.hasText(orderSearch.getMemberName())) {  // orderSearch에 무슨 값이 있으면
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			
			jpql += " m.name like :name";
		}
		
		// 3)
		TypedQuery<Order> query = em.createQuery(jpql, Order.class)
				.setMaxResults(1000);
		
		// 4)
		if(orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		
		// 5)
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		
		// 6)
		return query.getResultList();
		
	}

	// 7. 조회 2 - JPA Criteria : JPA가 표준으로 제공해주는 방법
	public List<Order> findAllByCriteria(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();  // 7-1. entity manager에서 builder를 꺼낸다
		
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);  // 7-2. Order를 조회하는 기본 쿼리를 만든다. ex) select o from Order o;
		
		Root<Order> o = cq.from(Order.class);  // 7-3. 시작 위치를 잡는다. 7-2에서 만든 쿼리를 시작 위치로 잡겠다는 말.
		
		Join<Object, Object> m = o.join("member", JoinType.INNER);  // 7-4. join("join으로 가져올 엔티티", "join Type")
		
		List<Predicate> criteria = new ArrayList<Predicate>();  // 7-5. 
		
		
		// 7-6. 주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			
			criteria.add(status);
		}
		
		// 7-7. 회원 이름 검색
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			
			Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
			
			criteria.add(name);
		}
		
		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		
		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
		
		return query.getResultList();
	}
	
	// 8. 조회 3 - Querydsl : 다음에 다시 다룰꺼임
	
}
