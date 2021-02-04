package com.shopJpa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopJpa.domain.item.Book;
import com.shopJpa.domain.item.Item;
import com.shopJpa.form.BookForm;
import com.shopJpa.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	
	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("itemForm", new BookForm());
		return "items/createItemForm";
	}
	
	@PostMapping("/items/new")
	public String create(BookForm form) {
		
		Book book = new Book();
		
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());
		
		itemService.saveItem(book);
		
		return "redirect:/";
	}
	
	@GetMapping("/items")
	public String list(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "items/itemList";
	}
	
	@GetMapping("items/{itemId}/edit")  // path variable {}
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		// 이번 예제에서는 Book만 가져온다고 가정
		Book item = (Book)itemService.findItem(itemId);
		
		BookForm form = new BookForm();
		form.setId(item.getId());
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setStockQuantity(item.getStockQuantity());
		form.setAuthor(item.getAuthor());
		form.setIsbn(item.getIsbn());
		
		model.addAttribute("updateForm", form);
		
		return "items/updateItemForm";
	}
	
	@PostMapping("items/{itemId}/edit")
	public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("updateForm") BookForm form) {
		// 1. 엔티티를 parameter로 받는 경우 -> 권장하지 않음.
//		Book book = new Book();
//		book.setId(form.getId());
//		book.setName(form.getName());
//		book.setPrice(form.getPrice());
//		book.setStockQuantity(form.getStockQuantity());
//		book.setAuthor(form.getAuthor());
//		book.setIsbn(form.getIsbn());
//		
//		itemService.saveItem(book);
		
		// 2. 필요한 데이터만 parameter로 받는 경우 -> 권장
		itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
		
		// 3. DTO를 만든다.
		
		return "redirect:/items";
	}
}
