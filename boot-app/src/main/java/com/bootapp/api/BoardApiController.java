package com.bootapp.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootapp.domain.Board;
import com.bootapp.exception.ResourceNotFoundException;
import com.bootapp.service.BoardService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000") // 5.
@RestController // 1.
@RequiredArgsConstructor // 2.
@RequestMapping("/api/v1/") // 3.
public class BoardApiController {

	private final BoardService boardService; // 4.

	/* 
	 * 6. 게시글 조회
	 */
	@GetMapping("/board")
	public List<Board> readBoardList() {
		return boardService.findBoardListService();
	}

	/* 
	 * 7. 게시글 글쓰기
	 */
	@PostMapping("/board")
	public Board createBoard(@RequestBody Board board) {
		return boardService.saveBoardWriteService(board);
	}

	/*
	 * 8. 게시글 조회 by id(수정이나 삭제를 위해서 필요)
	 */
	@GetMapping("/board/{id}")
	public ResponseEntity<Board> readBoardById(@PathVariable Long id) {
		boardService.upHitService(id);

		Board board = boardService.findBoardService(id)
							.orElseThrow(() -> new ResourceNotFoundException("Board not exist with id : " + id));

		return ResponseEntity.ok(board);
	}

	/*
	 * 9. 게시글 정보 수정
	 */
	@PutMapping("/board/{id}")
	public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board boardDetails) {

		Board board = boardService.findBoardService(id)
							.orElseThrow(() -> new ResourceNotFoundException("Board not exist with id : " + id));

		board.setTitle(boardDetails.getTitle());
		board.setContent(boardDetails.getContent());

		Board updateBoard = boardService.saveBoardWriteService(board);

		return ResponseEntity.ok(updateBoard);
	}

	/*
	 * 10. 게시글 삭제
	 */
	@DeleteMapping("/board/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteBoard(@PathVariable Long id) {
		Board board = boardService.findBoardService(id)
							.orElseThrow(() -> new ResourceNotFoundException("Board not exist with id : " + id));

		boardService.removeBoardService(board);
		
		Map<String, Boolean> response = new HashMap<>();

		response.put("deleted", Boolean.TRUE);

		return ResponseEntity.ok(response);

	}

	/*
	 * 11. 게시글 조회(페이징)
	 */
	@GetMapping("/pagelist/{page}/{size}")
	public Page<Board> list(@PathVariable int page, @PathVariable int size) {
		System.out.println(page + ", " + size);
		return boardService.pageListService(page, size);
	}
	
}