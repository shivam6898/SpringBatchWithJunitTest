package com.app.controller;

import com.app.model.Employee;
import com.app.service.EmployeeService;
import com.sun.istack.NotNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("insertinbatch")
	public ResponseEntity<?>  inserEmpoyeeInBatch( @NotNull @RequestBody List<Employee> empList) {
		List<Employee> insertedList = employeeService.insertEmployeeInBatch(empList);
		LOGGER.info("employee_inserted {}",insertedList);
		if ((insertedList != null && insertedList.isEmpty())) {
			return new ResponseEntity<String>("NO DATA FOUND TO INSERT", HttpStatus.NO_CONTENT);
		} else return new ResponseEntity<List<Employee>>(insertedList, HttpStatus.OK);
	}

	//adding pagegnation and page always starts with 0
	@GetMapping("/getall/{pageNo}/{pageSize}")
	public ResponseEntity<?>  getAllEmployees(@NotNull @PathVariable int pageNo, @PathVariable int pageSize){
		Page<Employee> list=employeeService.getAllEmployees(pageNo,pageSize);
		if((list != null && list.isEmpty())) {
			return new ResponseEntity<String>("NO DATA FOUND",HttpStatus.NO_CONTENT);
		}else return new ResponseEntity<Page<Employee>>(list, HttpStatus.OK);
	}

	@PutMapping("update")
	public ResponseEntity<?> updateEmployee(@NotNull @RequestBody List<Employee> employee) {
		val updatedList = employeeService.updateAll(employee);
		LOGGER.info("Employees updated {}",updatedList);
		if(updatedList != null && updatedList.isEmpty()) {
			return new ResponseEntity<String>("NO DATA FOUND",HttpStatus.NO_CONTENT);
		}else return new ResponseEntity<List<Employee>>(updatedList, HttpStatus.OK);
	}

	@DeleteMapping("deleteinbatch")
	public ResponseEntity<String> deleteEmployeeInBatch(@NotNull @RequestBody List<Employee> empList) {
		LOGGER.info("Fetching & Deleting User with id {}", empList);
		LOGGER.warn("  "  );
		String result=employeeService.deleteInBatch(empList);
		return new ResponseEntity<String>(result,HttpStatus.OK);
	}
}