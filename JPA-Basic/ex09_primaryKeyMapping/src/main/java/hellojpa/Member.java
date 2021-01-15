package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

@Entity(name = "member")
// 시퀀스 
// @SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", allocationSize = 1)  // 3. 시퀀스 지정 -> 하이버네이트에서 원하는 sequence 이름으로 sequence를 만들어 적용하고 싶을 때, 증가폭을 지정해주어야 된다. -> allocationSize = 1
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", allocationSize = 50)  // 7. 성능 최적화 : allocationSize = 50(default) 

// Table 전략 : 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략(장점: 모든 데이터베이스에 적용 가능, 단점: 성능)
@TableGenerator(name = "member_seq_generator", table = "my_sequences", pkColumnValue = "member_seq", allocationSize = 50)  // 4.
public class Member {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE) // 1. 하이버네이트 시퀀스 적용(오라클) -> 오라클 DB 시퀀스에 hibernate_sequence가 만들어진다.
	
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")  // 2. 내가 지정한 이름(member_seq)의 sequence를 만들어준다(하나 주의 할 점은 create 속성으로 바꾼다음 실행하면 오류가 뜨는데 기존에 member_seq라는 시퀀스가 없어서 그런 것 이다(오류는 뜨는데 sequence를 생성해준다. 만약 기존에 저 이름의 시퀀스가 있다면 오류 안뜸)).

//	@GeneratedValue(strategy = GenerationType.TABLE, generator = "member_seq_generator") // 5. 시퀀스를 테이블로 만들어서 적용함.

//	@GeneratedValue(strategy = GenerationType.IDENTITY) // 6. p.k값을 알려면 DB에 데이터가 들어가봐야 알 수 있기 때문에 IDENTITY 전략에서만 예외적으로 persist 메소드가 호출되었을 때 insert 쿼리가 날라간다.(보통은 커밋시에 쿼리가 날라감) -> 얘는 mysql쓸 때 써보자.(오라클에선 안된다.) 
	private Long id;  // 주의 : 오라클에서는 기본키 타입을 String으로 하면 오류난다.(SEQUENCE가 숫자 타입이라 그런거 같다.(내꺼에선 String으로 지정하고 돌리면 오류남))
	
	@Column(name = "name", nullable = false)
	private String username;
	
	public Member() {
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
	
	
}
