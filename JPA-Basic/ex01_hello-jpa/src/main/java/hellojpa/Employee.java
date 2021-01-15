package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity  // 1. jpa로 인식해주는 어노테이션
public class Employee {
	
	@Id  // 2. jpa한테 가지고올 테이블의 p.k(기본키)가 무엇인지 알려주는 어노테이션
	private int id;
	
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
