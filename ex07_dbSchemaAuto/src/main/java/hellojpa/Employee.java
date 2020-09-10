package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "employee")  // 1. jpa로 인식해주는 어노테이션(jpa가 관리하는 객체)
@Table(name = "emp")  // 3. 데이터베이스에 있는 테이블의 이름 값을 바꿔주는 어노테이션.(사용자마다 테이블명이 다를 수가 있으므로 그럴 때 사용), *주의 : 속성이 일치해야 한다.
public class Employee {
	
	@Id  // 2. jpa한테 가지고올 테이블의 p.k(기본키)가 무엇인지 알려주는 어노테이션
	private int id;
	
	@Column(length = 20)  // 6. @Column 어노테이션만 새로 붙여서 update 속성으로 돌리면 추가가 안된다. create로 삭제했다 다시 만들어야 된다.
	private String name;
	
	private int age;  // 4. 하나를 더 만들고 persistence.xml에서 hibernate.hbm2ddl.auto를 설정하고 속성을 create로 두면 DB를 지우고 속성을 추가해서 다시 만들어 준다.
	
	@Column(unique = true, length = 20) // 5. update는 구조를 바꾸진 못한다(컬럼을 추가하는건 가능.). 
	private String gender;
	
	private String hits; // 7. update 속성은 단순 속성 추가는 되는데 이미 만들어진 속성을 바꾸진 못한다.(create로 다시만들어야함)
	
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}
	
	
}
