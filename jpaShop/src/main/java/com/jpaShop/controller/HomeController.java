package com.jpaShop.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j  // 2. 로거 찍어보기 2 -> lombok 사용
public class HomeController {
	
	// 1. 로거 찍어보기 1 -> org.slf4j.Logger
//	Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping("/")
	public String home() {
		log.info("home controller");
		return "home";
	}
}
