package com.jpaShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("hello")
	public String hello(Model model) {
		model.addAttribute("data", "hello!!!");
		return "hello";  // thymeleaf 사용시 resources/templates 하위에 view 파일들을 찾아간다.
	}
}
