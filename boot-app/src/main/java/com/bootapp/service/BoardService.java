package com.bootapp.service;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootapp.domain.Board;
import com.bootapp.repository.BoardRepository;

import lombok.RequiredArgsConstructor;


@Service // 1. component scan의 대상임을 명시
@Transactional(readOnly = true) // 2. JPA는 트랜잭션 안에서 동작한다.
@RequiredArgsConstructor // 3. final이 붙은 곳에다가 생성자를 통한 주입을 해준다.(lombok)
public class BoardService {

	private final BoardRepository boardRepository;

	/*
	 * 4. 게시글 전체 조회 서비스
	 */
	@Transactional(readOnly = true) // 5. 전체 클래스에 트랜잭션을 걸어주고 @Transactional(readOnly = true) -> 이걸 설정해 놓으면 JPA가 성능을 최적화 해준다.
	public List<Board> findBoardListService() {
		// return boardRepository.findAll();
		return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

	}

	/*
	 * 6. 게시글 글쓰기 서비스(update에도 사용) 
	 */
	@Transactional(readOnly = false) // 7. 생성한 뒤 롤백 false
	public Board saveBoardWriteService(Board board) {
		return boardRepository.save(board);
	}


	/*
	 * 7. 사원 단건 조회 서비스
	 */
	public Optional<Board> findBoardService(Long id) {
		return boardRepository.findById(id);
	}

	/*
	 * 8. 사원 단건 조회 서비스
	 */
	@Transactional(readOnly = false) // 11. 걸어줘야 롤백 안함.
	public void removeBoardService(Board board) {
		boardRepository.delete(board);
	}

	/*
	 * 9. 조회수 증가 서비스
	 */
	@Transactional(readOnly = false)
	public int upHitService(Long id) {
		return boardRepository.upHit(id);
	}
	
	/*
	 * 10. 사원 조회(페이징)
	 */

	@Transactional(readOnly = true) 	
	public Page<Board> pageListService(int page, int size) {
		Pageable pageable = PageRequest.of(page, size); // page는 시작 페이지 번호(0 페이지부터 시작), size는 한페이지당 보여줄 게시글 갯수

		return boardRepository.findAll(pageable); // PageRequest.of(page, size, sort) 
	}

}