package com.app;


import com.app.model.Employee;
import com.app.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class EmployeeTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;


    
    /*@Test
    public void getEmployeeTest() throws Exception{
        Page<Employee> empPage = employeeService.getAllEmployees(0, 1);
        given(employeeService.getAllEmployees(0,1)).willReturn(empPage);
        this.mockMvc.perform(get("/emp/getall/0/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empSal").value("emp1"));
    }*/

    @Test
    public void employeeDeleteTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(delete("/emp/deleteinbatch"));
        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void testFindAllEmployees() {
        Employee emp1 = new Employee(1, "emp1", 4.2);
        Employee emp2 = new Employee(2, "emp2", 5.2);
        Employee emp3 = new Employee(3, "emp3", 5.2);
        List<Employee> list = Arrays.asList(emp1, emp2, emp3);
        Page<Employee> page = new PageImpl<>(list);
        when(employeeService.getAllEmployees(0, 2)).thenReturn(page);
        Page<Employee> resultPage = employeeService.getAllEmployees(0, 2);
        assertThat(resultPage.getSize()).isEqualTo(3);
    }

    @Test
    public void insertEmployeeTest() throws Exception {
        Employee emp1 = new Employee();
        emp1.setEmpName("emp1");
        emp1.setEmpSal(4.2);
        List<Employee> empList = Arrays.asList(emp1);
        String inputInJson = TestUtils.mapToJson(empList);
        Mockito.when(employeeService.insertEmployeeInBatch(Mockito.any())).thenReturn(empList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/emp/insertinbatch")
                .accept(MediaType.APPLICATION_JSON).content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String outputInJson = response.getContentAsString();

        assertThat(outputInJson).isEqualTo(inputInJson);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    public void updateEmployeeTest() throws Exception {
        Employee employee = new Employee();
        employee.setEmpId(1);
        employee.setEmpName("updated");
        employee.setEmpSal(5.2);
        List<Employee> list = Arrays.asList(employee);
        String inputJson = TestUtils.mapToJson(list);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/emp/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        /*String content = mvcResult.getResponse().getContentAsStrin*/
    }

    @Test
    public void  updateEmployeeTest2(){
        List<Employee> empList = Arrays.asList(new Employee(1, "name", 4.5));
        when(employeeService.insertEmployeeInBatch(empList)).thenReturn(empList);
        List<Employee> updatedList= Collections.singletonList(new Employee(1, "updatedname", 4.5));
        when(employeeService.updateAll(Arrays.asList(new Employee(1,"updatedname",4.5)))).thenReturn(updatedList);
        assertNotEquals(updatedList,empList);
    }

    @Test
    public void deleteAllTest() throws Exception{
        //mockMvc.perform(delete("/emp/deleteinbatch")).andExpect(status().isOk()).andExpect(content().string("delete in batch successful"));
        List<Employee> empList = Arrays.asList(new Employee(1, "name", 4.5),new Employee(2, "name2", 5.5));
        when(employeeService.insertEmployeeInBatch(empList)).thenReturn(empList);
        when(employeeService.deleteInBatch(empList)).thenReturn("delete in batch successful");
    }



}
