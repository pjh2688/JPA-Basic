package com.shopJpa.repository.order.simplequery;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

	/**
	 * 4. JPA에서 DTO로 바로 조회 : com.shopJpa.repository.order.simplequery.OrderSimpleQueryDto -> 주의 : new 오퍼레이션에선 entity를 바로 넘기는건 안된다. 식별자로 넘어가기 때문
	 */
	
	private final EntityManager em;
	
	public List<OrderSimpleQueryDto> findOrderDtos() {
		return em.createQuery("select new com.shopJpa.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"+ 
						" from Order o" +
						" join o.member m" + 
						" join o.delivery d", OrderSimpleQueryDto.class).getResultList();
	}
	
}
/*
 	-JPA에서 DTO로 바로 조회
 	1. 장점
 	 - 일반적인 SQL을 사용할 때 처럼 원하는 값을 선택해서 조회
 	 - new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 반환.
 	 - SELECT 절에서 원하는 데이터(DTO)를 직접 선택하므로 DB와 애플리케이션 간 네트워크 용량을 최적화(생각보다 미비)
 	2. 단점
 	 - repository의 재사용성이 떨어진다 -> API 스펙에 맞춰진 코드가 repository에 들어가기 때문에 
 */
