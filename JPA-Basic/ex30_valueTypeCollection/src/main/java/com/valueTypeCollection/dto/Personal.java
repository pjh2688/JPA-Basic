package com.valueTypeCollection.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "personal")
@SequenceGenerator(name = "personal_seq_generator", sequenceName = "personal_seq", allocationSize = 50)
public class Personal {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personal_seq_generator")
	@Column(name = "PERSONAL_ID")
	private Long id;
	
	@Column(name = "PERSONAL_NAME")	
	private String name;
	
	@Embedded
	private Address homeAddress;
	
	@ElementCollection  // 값 타입과 매핑, 외래 키 설정 -> joinColumns
	@CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "PERSONAL_ID"))  // @JoinColumn(name = "PERSONAL_ID") -> 속성 name이 없지만 name으로 해야 테이블맵핑이 제대로 된다.
	@Column(name = "FOOD_NAME")
	private Set<String> favoriteFoods = new HashSet<String>(); 

//	@ElementCollection
//	@CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "PERSONAL_ID"))
//	private List<Address> addressHistory = new ArrayList<Address>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)  // 두 옵션을 모두 활성화 시키면 부모 엔티티를 통해서 자식의 생명주기도 관리할 수 있다. -> 부모랑 자식 관계에서 부모만 persist하고 자식은 persist를 안해도 된다. 부모만 persist를 해도 자식은 자동으로 persist 된다는 말.
	@JoinColumn(name = "PERSONAL_ID")
	private List<AddressEntity> addressHistory = new ArrayList<AddressEntity>();
	
	public Personal() {
		// TODO Auto-generated constructor stub
	}

	public Set<String> getFavoriteFoods() {
		return favoriteFoods;
	}

	public void setFavoriteFoods(Set<String> favoriteFoods) {
		this.favoriteFoods = favoriteFoods;
	}

//	public List<Address> getAddressHistory() {
//		return addressHistory;
//	}
//
//	public void setAddressHistory(List<Address> addressHistory) {
//		this.addressHistory = addressHistory;
//	}
	
	public List<AddressEntity> getAddressHistory() {
		return addressHistory;
	}

	public void setAddressHistory(List<AddressEntity> addressHistory) {
		this.addressHistory = addressHistory;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}
	
	
}
