package com.app.service;

import com.app.controller.EmployeeController;
import com.app.model.Employee;
import com.app.repository.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	private EmployeeRepo employeeRepository;

	@Transactional
	public List<Employee> insertEmployeeInBatch(List<Employee> empList) {
		long start = System.currentTimeMillis();
		List<Employee> insertedList = employeeRepository.saveAll(empList);
		LOGGER.info("Time taken by InsertInBatch--{}",(System.currentTimeMillis() - start));
		return insertedList;
	}


	@Transactional
	public List<Employee> updateAll(List<Employee> employee) {
		long start = System.currentTimeMillis();
		List<Employee> updatedEmployee=employeeRepository.saveAll(employee);
		LOGGER.info("Time taken by UpdateInBatch--{}",(System.currentTimeMillis() - start));
		return updatedEmployee;
	}


	public Page<Employee> getAllEmployees(int pageno,int pageSize) {
		long start = System.currentTimeMillis();
		Pageable pagewithelements= PageRequest.of(pageno,pageSize);
		Page<Employee> retreivedEmployee=employeeRepository.findAll(pagewithelements);
		LOGGER.info("Time taken by to get all employees--{}",(System.currentTimeMillis() - start));
		return retreivedEmployee;
	}

	@Transactional
	public String deleteInBatch(List<Employee> empList) {
		long start = System.currentTimeMillis();
		employeeRepository.deleteInBatch(empList);
		LOGGER.info("Time taken by to get all employees--{}",(System.currentTimeMillis() - start));
		return "delete in batch successful";
	}


}
