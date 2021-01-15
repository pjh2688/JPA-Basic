package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;
// import javax.persistence.Table;

@Entity(name = "employee")  // 1. jpa로 인식해주는 어노테이션(jpa가 관리하는 객체)
// @Table(name = "emp")  // 3. 데이터베이스에 있는 테이블의 이름 값을 바꿔주는 어노테이션.(사용자마다 테이블명이 다를 수가 있으므로 그럴 때 사용), *주의 : 속성이 일치해야 한다.
public class Employee {
	
	@Id  // 2. jpa한테 가지고올 테이블의 p.k(기본키)가 무엇인지 알려주는 어노테이션
	private int id;
	
	private String name;
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}
	
	public Employee(int id, String name) {
		this.id = id;
		this.name = name;
	}

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
