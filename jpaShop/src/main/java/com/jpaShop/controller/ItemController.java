package com.jpaShop.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.jpaShop.dto.Item;
import com.jpaShop.dto.item.Book;
import com.jpaShop.form.BookForm;
import com.jpaShop.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor  // 2.
public class ItemController {

	// 1.
	private final ItemService itemService;
	
	// 3. 폼
	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("BookForm", new BookForm());
		return "items/createItemForm";
	}
	
	// 4. 등록 
	@PostMapping("/items/new") // 5. @Valid 어노테이션이 있으면 해당 객체에 사용된 validation에 설정된 어노테이션을 실행시킨다. 
	public String create(@Valid BookForm form, BindingResult result) { // 6. BindingResult 객체가 @Valid어노테이션이 쓰인 위치에 매개변수로 있으면 오류 내용이 넘어와 result변수에 저장된다..(Default 에러페이지로 안넘어가고)
		
		Book book = new Book();
		
		// 7. 다음에는 setter를 없애고 함수로 묶어주는게 좋다.
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());
		
		itemService.saveItem(book);
		
		return "redirect:/";
	}
	
	// 8. 조회
	@GetMapping("/items")
	public String list(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "items/itemList";
		
	}
	
	// 9. 수정 화면
	@GetMapping("/items/{itemId}/edit")  // 10. path variable
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		
		
		Item item =(Book)itemService.find(itemId);
		
	
		Book book = (Book)item;
			
		BookForm form = new BookForm();
		form.setId(book.getId());
		form.setName(book.getName());
		form.setPrice(book.getPrice());
		form.setStockQuantity(book.getStockQuantity());
		form.setAuthor(book.getAuthor());
		form.setIsbn(book.getIsbn());
		
		model.addAttribute("BookForm", form);
		
		return "items/updateBookForm";
		
	}
	
	// 10. 수정
	@PostMapping("/items/{itemId}/edit")  // 10. path variable
	public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("BookForm") BookForm form) {
		
		// 1. 
//		Book book = new Book();
//		
//		book.setId(form.getId());
//		book.setName(form.getName());
//		book.setPrice(form.getPrice());
//		book.setStockQuantity(form.getStockQuantity());
//		book.setAuthor(form.getAuthor());
//		book.setIsbn(form.getIsbn());
//		
//		itemService.saveItem(book);

		// 2.
		itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
		
		return "redirect:/items";
	}
}
