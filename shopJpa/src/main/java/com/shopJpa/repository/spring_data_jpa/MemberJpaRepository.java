package com.shopJpa.repository.spring_data_jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopJpa.domain.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	// select m from Member m where m.name = :name; -> jpql을 spring data jpa에서 동적으로 알아서 만들어준다.
	List<Member> findByName(String name);
}
