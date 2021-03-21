package com.bootapp.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootapp.domain.Employee;
import com.bootapp.exception.ResourceNotFoundException;
import com.bootapp.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000") // 5. 리액트 연동시 필요.
@RestController // 1.
@RequiredArgsConstructor // 2.
@RequestMapping("/api/v1/") // 3.
public class EmployeeApiController {

	private final EmployeeService employeeService; // 4.

	/*
	 * 6. 사원 전체 조회
	 */
	@GetMapping("/employees")
	public List<Employee> readEmployees() {
		return employeeService.findEmployeesService();
	}

	/*
	 * 7. 사원 생성
	 */
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployeeService(employee);
	}

	/*
	 * 8. 사원 조회 by id(수정이나 삭제를 위해서 필요)
	 */
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> readEmployeeById(@PathVariable Long id) {
		Employee employee = employeeService.findEmployeeService(id)
									.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id : " + id));
		
		return ResponseEntity.ok(employee);
	}

	/*
	 * 8. 사원 정보 수정
	 */
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
		Employee employee = employeeService.findEmployeeService(id)
									.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id : " + id));
		
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmail(employeeDetails.getEmail());

		Employee updateEmployee = employeeService.saveEmployeeService(employee);

		return ResponseEntity.ok(updateEmployee);
	}

	/*
	 * 9. 사원 정보 삭제
	 */
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployeeById(@PathVariable Long id) {
		Employee employee = employeeService.findEmployeeService(id)
									.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id : " + id));

		employeeService.removeEmployeeService(employee);

		Map<String, Boolean> response = new HashMap<>();

		response.put("deleted", Boolean.TRUE);

		return ResponseEntity.ok(response);
	}
}
