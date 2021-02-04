package com.shopJpa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopJpa.domain.Member;
import com.shopJpa.domain.Order;
import com.shopJpa.domain.OrderSearch;
import com.shopJpa.domain.item.Item;
import com.shopJpa.service.ItemService;
import com.shopJpa.service.MemberService;
import com.shopJpa.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;
	
	@GetMapping("/order")
	public String createForm(Model model) {
		
		List<Member> members = memberService.findMembers();
		List<Item> items = itemService.findItems();
		
		model.addAttribute("members", members);
		model.addAttribute("items", items);
		
		return "order/createOrderForm";
	}
	
	// @RequestParam -> form 형식으로 넘어올때 지정된 name과 매핑
	@PostMapping("/order")
	public String order(@RequestParam("memberId") Long memberId, 
						@RequestParam("itemId") Long itemId,
						@RequestParam("count") int count) {
		
		orderService.order(memberId, itemId, count);
		
		return "redirect:/orders";
	}
	
	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {  // 상품 리스트 검색에 대한 정보들이 orderSearch로 넘어온다.
		List<Order> orders = orderService.searchOrders(orderSearch);
		
		// 2021-01-21 ->  상품 리스트를 뿌려줄때 현재 대표상품만 보여주게 되어 있는데 상품 전체를 뷰에 뿌려주는걸 다음에 해야한다. 일단 여기서는 주문 번호를 가져와서 그 주문 번호에 따른 주문 상품의 갯수를 출력해 보았다.
		for(int i = 0; i < orders.size(); i++) {
			for(int j = 0; j < orders.get(i).getOrderItems().size(); j++) {
				System.out.println(i+"번 주문 상품 내역 --");
				System.out.println(orders.get(i).getOrderItems().get(j).getItem());
			}
		}
		
		model.addAttribute("orders", orders);
		return "order/orderList";
	}
	
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
}
