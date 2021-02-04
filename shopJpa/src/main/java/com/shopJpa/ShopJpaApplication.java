package com.shopJpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@SpringBootApplication
public class ShopJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopJpaApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {
	//	1. Lazy Loading 무시 O
	//	return new Hibernate5Module();
		
	//  2. Lazy Loading 무시 X(json 데이터를 엔티티에 lazy Loading이 걸려있어도 무시하고 데이터를 lazy loading이 안걸린 상태로 생각함)
		Hibernate5Module hibernate5Module = new Hibernate5Module();
	//	hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
			
		return hibernate5Module;
	}

}
