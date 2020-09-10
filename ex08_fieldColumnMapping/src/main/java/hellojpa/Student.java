package hellojpa;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name = "student") // 1. jpa로 인식해주는 어노테이션(jpa가 관리하는 객체)
public class Student {

	@Id // 2. jpa한테 가지고올 테이블의 p.k(기본키)가 무엇인지 알려주는 어노테이션
	private Long id;
	
	// 3. dto클래스에선 username이라는 변수 명으로 사용하고 실제 데이터베이스 컬럼명은 name으로 쓰고 싶을 때 @Column 사용
	@Column(name = "name", nullable = true)
	private String username;
	
	private Integer age; 
	
	// 4. Enum 타입 사용하기 위해 @Enumerated 어노테이션을 붙이면 DB에서 가지고온 데이터를 Enum타입으로 자동으로 바꿔준다.(JPA의 기능)
	@Enumerated(EnumType.STRING)  // 주의 : EnumType.ORDINAL은 사용하지 말 것.
	private RoleType roleType;
	
	// 5. 날짜 타입(Date(날짜), TIME(시간), TIMESTAMP(날짜와 시간)) -> hibernate 과거 버전 사용시 날짜는 이렇게 표현해야함. 
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;
	
	// 6. varchar 타입 중에 매우 큰 용량의 varchar를 대체해준다. -> @Lob은 지정할 수 있는 속성이 없고, 매핑하는 필드가 문자이면 CLOB으로 매핑, 나머지는 BLOB으로 매핑시켜준다.(CLOB : String, char[], BLOB : Byte[] 등)
	@Lob
	private String description;
	
	// 7. DB랑 관계 없이 프로그램 안에서만 사용할 변수를 지정하는 어노테이션(필드 매핑 X, 데이터베이스에 저장이나 조회 X(주로 프로그램 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용)
	@Transient
	private int temp;
	
	// 8. 최신버전 hibernate를 사용한다면 어노테이션 없이 LocalDate이나 LocalDateTime 타입으로 dto 변수를 만들면 어노테이션을 따로 안 붙여도 hibernate.hbm2ddl.auto 속성으로 날짜 컬럼이 데이터베이스에 자동 생성 된다.
 // private LocalDate localDate;  // 년월
 // private LocalDateTime localDateTime; // 년월일

	public Student() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
