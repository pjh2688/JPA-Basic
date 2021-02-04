package com.shopJpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j  // 2. Lombok @Slf4j 어노테이션 활용.
public class HomeController {

//  Logger log = LoggerFactory.getLogger(getClass());  // 1. 로그 찍는 방법 1(org.slf4j)
	
	@RequestMapping("/")
	public String home() {
		log.info("HomeController");
		
		return "home";
	}
	
	
}