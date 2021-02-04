package com.shopJpa.repository;

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

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderSearch;
import com.shopJpa.domain.OrderStatus;
import com.shopJpa.domain.QMember;
import com.shopJpa.domain.QOrder;
import com.shopJpa.repository.order.simplequery.OrderSimpleQueryDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;
	
	public void save(Order order) {
		em.persist(order);
	}
	
	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}
	
	/**
	 * 1-1. 동적 쿼리 : 무식한 방법
	 */
	public List<Order> findAllByString(OrderSearch orderSearch) {
		
		// 1-2.
		String jpql = "select o from Order o join o.member m";
		
		boolean isFirstCondition = true;  // 1-3. 밑에 if문이 무조건 한번은 돌아서 where을 붙이고 시작하게 하기 위해 true
		
		// 1-4. 주문 상태 검색(검색 조건이 없으면 모두 가져온다.)
		if(orderSearch.getOrderStatus() != null) { 
			
			if(isFirstCondition) { // 1-5. 무조건 한 번은 돌기 때문에 where을 처음에 붙여준다.
				jpql += " where ";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";  // 1-6. 파라미터 바인딩
		}
		
		// 1-7. 회원 이름 검색(검색 조건이 없으면 모두 가져온다.)
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			
			if(isFirstCondition) { 
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}
		
		TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);  // 최대 1000개까지 조회
		
		if(orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		
		return query.getResultList();
		
	}
	
	/**
	 * 2. 동적 쿼리 : JPA Criteria(JPA 표준) -> 권장하지 않음.
	 */
	public List<Order> findAllByCriteria(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		
		Root<Order> o = cq.from(Order.class);
		
		Join<Object, Object> m = o.join("member", JoinType.INNER);
		
		List<Predicate> criteria = new ArrayList<>();
		
		// 주문 상태 검색
		if(orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			criteria.add(status);
		}
		
		// 회원 이름 검색
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}
		
		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		
		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
		
		return query.getResultList();
	}

	/**
	 * 3. 엔티티를 DTO로 변환 - 페치 조인으로 최적화 
	 */
	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery("select o from Order o" + 
						" join fetch o.member m" + 
						" join fetch o.delivery d", Order.class).getResultList();
		
	}
	
	/**
	 * 4. JPA에서 DTO로 바로 조회 : com.shopJpa.repository.order.simplequery.OrderSimpleQueryDto -> 주의 : new 오퍼레이션에선 entity를 바로 넘기는건 안된다. 식별자로 넘어가기 때문
	 */
	public List<OrderSimpleQueryDto> findOrderDtos() {
		return em.createQuery("select new com.shopJpa.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"+ 
						" from Order o" +
						" join o.member m" + 
						" join o.delivery d", OrderSimpleQueryDto.class).getResultList();
	}
	
	/**
	 * 5. 엔티티를 DTO로 변환 - 페치 조인 최적화
	 */
	public List<Order> findAllWithItem() {
		
		/*
		  	* jpql distinct 키워드의 역할 
		  	 (1) DB에 실제로 distinct 를 붙여서 쿼리를 날려보내준다.(실제 데이터베이스에서는 뻥튀기 된다.(DB QUERY에서는) 그러나 JPA에서는 jpql로 가지고 올때는 중복을 제거 해준다.)
		  	 (2) jpql안에서는 from 뒤에 있는 엔티티를 DB에서 데이터를 가져왔을 때 데이터가 중복되면 중복을 걸러서 컬렉션에 담아준다.
		 */
		return em.createQuery("select distinct o from Order o" + 
							  " join fetch o.member m" + 
							  " join fetch o.delivery d" + 
							  " join fetch o.orderItems oi" + 
							  " join fetch oi.item i", Order.class).getResultList();
		
	}
	
	/*
	 	- 참고 
	 	(1) 컬렉션 페치 조인을 사용하면 원칙적으로 페이징이 불가능하다. 그래서 하이버네이트에서는 경고 로그를 남겨주고
	 	         모든 데이터를 DB에서 읽어온 뒤, 메모리에서 페이징 처리를 해버린다.(매우 위험하다)
	 	(2) 컬렉션 페치 조인은 1개만 사용할 수 있다. 엔티티안에 둘 이상의 컬렉션에 있다고 2개이상 컬렉션 페치조인을 사용하면 안된다.
	 	    2개 이상 컬렉션에 페치조인을 사용하면  (JPA에서)데이터가 부정합하게 조회될 수 있기 때문이다.
	
	 	*트레이드오프[trade off] : 두 개의 정책목표 가운데 하나를 달성하려고 하면 다른 목표의 달성이 늦어지거나 희생되는 경우의 양자간의 관계.

	 */
	
	/**
	 * 6. 엔티티를 DTO로 변환 - 페이징과 한계 돌파 
	 */
	public List<Order> findAllWithMemberDelivery(int offset, int limit) {

		
		return em.createQuery("select o from Order o" + 
							  " join fetch o.member m" + 
							  " join fetch o.delivery d", Order.class)
							.setFirstResult(offset)
							.setMaxResults(limit)
							.getResultList();
		/*
		// 페치조인을 안써도 되긴 하는데 쿼리가 많이 나감.
		return em.createQuery("select o from Order o", Order.class)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
		*/
	}
	
	/**
	 * 7. 동적 쿼리 : QueryDSL
	 */
	public List<Order> findAllByQuerydsl(OrderSearch orderSearch) {
		// 7-1. 만들어진 QOrder와 QMember를 꺼내온다. -> static import
		QOrder order = QOrder.order;	
		QMember member = QMember.member;
		
		// 7-2.
		JPAQueryFactory query = new JPAQueryFactory(em);
		
		return query.select(order)  // 7-3. QOrder select
			.from(order)  // 7-4. QOrder로 부터 
			.join(order.member, member)  // 7-5. QOrder에 있는 member와 join하는데 alias를 member로 설정한다는 말.
//			.where(statusEq(orderSearch.getOrderStatus()))  // 7-7. 
//			.where(order.status.eq(orderSearch.getOrderStatus()))  // 7-8. 정적 쿼리
//			.where(statusEq(orderSearch.getOrderStatus()), member.name.like(orderSearch.getMemberName()))  // 7-9. 정적 쿼리
			.where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName())) // 7-11
			.limit(1000)
			.fetch();
	}
	
	// 7-10. condition 넣어보기(동적 쿼리)
	private BooleanExpression nameLike(String name) {
		
		if(!StringUtils.hasText(name)) {
			return null;
		}
		
		return QMember.member.name.like(name);
	}
	
	// 7-6. condition 넣어보기(동적 쿼리)
	private BooleanExpression statusEq(OrderStatus statusCond) {
		if(statusCond == null) {
			return null;
		}
		
		return QOrder.order.status.eq(statusCond);
	}
	
	// 7-12. 나중에 버전 관리 할때는 src/main/generated(querydsl)을 제외시키고 커밋해야된다.
}
