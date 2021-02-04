package com.jpaShop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jpaShop.dto.Item;
import com.jpaShop.dto.Member;
import com.jpaShop.dto.Order;
import com.jpaShop.dto.OrderSearch;
import com.jpaShop.service.ItemService;
import com.jpaShop.service.MemberService;
import com.jpaShop.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor  // 1. 
public class OrderController {

	// 2. 주문을 할때 고객,아이템을 선택할 수 있어야 하므로 dependency가 많이 필요하다.
	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;
	
	// 3. 주문(order) 폼 열기
	@GetMapping("/order")
	public String createForm(Model model) {
		
		// 4. 주문을 하기 위해 등록된 회원 목록을 가지고 온다.
		List<Member> members = memberService.findMembers();
		
		// 5. 주믄을 하기 위해 아이템 목록을 가지고 온다.
		List<Item> items = itemService.findItems();
		
		// 6. model에 4,5번을 싣는다.
		model.addAttribute("members", members);
		model.addAttribute("items", items);
		
		return "order/createOrderForm";
	}
	
	// 4. 주문(order)
	@PostMapping("/order")
	public String order(@RequestParam("memberId") Long memberId,
						@RequestParam("itemId") Long itemId,
						@RequestParam("count") int count) {
		orderService.order(memberId, itemId, count);
		return "redirect:/orders";
	}
	
	
	// 5. 주문목록
	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
		List<Order> orders = orderService.findOrders(orderSearch);
		
		model.addAttribute("orders", orders);
		return "order/orderList";
	}
	
	// 6. 주문취소
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
}

/*
 * Tip : 
  => mapping 할때 '/'로 시작하면 절대경로가 되고, '/'로 시작하지 않으면 
        현재 url 주소를 기준으로 경로가 변동됩니다. 그래서 리소스(css,js)에는 대부분 절대경로를 사용합니다.
 */