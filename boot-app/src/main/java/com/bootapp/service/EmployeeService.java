package com.bootapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootapp.domain.Employee;
import com.bootapp.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;


@Service // 1. component scan의 대상임을 명시
@Transactional(readOnly = true) // 2. JPA는 트랜잭션 안에서 동작한다.
@RequiredArgsConstructor // 3. final이 붙은 곳에다가 생성자를 통한 주입을 해준다.(lombok)
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	/*
	 * 4. 사원 전체 조회 서비스
	 */
	@Transactional(readOnly = true) // 5. 전체 클래스에 트랜잭션을 걸어주고 @Transactional(readOnly = true) -> 이걸 설정해 놓으면 JPA가 성능을 최적화 해준다.
	public List<Employee> findEmployeesService() {
		return employeeRepository.findAll();
	}

	/*
	 * 6. 사원 생성 서비스(update에도 사용) 
	 */
	@Transactional(readOnly = false) // 7. 생성한 뒤 롤백 false
	public Employee saveEmployeeService(Employee employee) {
		return employeeRepository.save(employee);
	}

	/*
	 * 8. 사원 단건 조회 서비스
	 */
	public Optional<Employee> findEmployeeService(Long id) {
		return employeeRepository.findById(id);
	}

	/*
	 * 9. 사원 정보 삭제 서비스
	 */
	@Transactional(readOnly = false) // 11. 걸어줘야 롤백 안함.
	public void removeEmployeeService(Employee employee) {
		employeeRepository.delete(employee);
	}

}
