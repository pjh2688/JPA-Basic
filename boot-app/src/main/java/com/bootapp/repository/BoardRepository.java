package com.bootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bootapp.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@Modifying
	@Query("update Board b set b.hit = b.hit + 1 where b.id = :id")
	public int upHit(Long id);

}
