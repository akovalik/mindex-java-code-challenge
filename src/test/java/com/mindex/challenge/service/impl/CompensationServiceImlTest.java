package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.DetailedCompensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImlTest {

    private String employeeUrl;
    private String compensationUrl;

    private String compensationIdUrl;

    private String detailedCompensationUrl;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        detailedCompensationUrl = "http://localhost:" + port + "/detailed_compensation/{id}";
    }

    @Test
    public void testCompensation() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Dough");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        //Create employee
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        //Create compensation
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setSalary("1234");
        testCompensation.setEffectiveDate("5/3/2023");

        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        assertNotNull(createdCompensation);
        assertCompensationEquivalence(testCompensation, createdCompensation);

        //Check read
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull(readCompensation);
        assertCompensationEquivalence(testCompensation, readCompensation);

        //Check detailed compensation
        DetailedCompensation detailedCompensation = restTemplate.getForEntity(detailedCompensationUrl, DetailedCompensation.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull(detailedCompensation);

        assertEquals(testEmployee.getFirstName(), detailedCompensation.getEmployee().getFirstName());
        assertEquals(testEmployee.getLastName(), detailedCompensation.getEmployee().getLastName());
        assertEquals(testEmployee.getDepartment(), detailedCompensation.getEmployee().getDepartment());
        assertEquals(testEmployee.getPosition(), detailedCompensation.getEmployee().getPosition());
        assertEquals(testCompensation.getSalary(), detailedCompensation.getSalary());
        assertEquals(testCompensation.getEffectiveDate(), detailedCompensation.getEffectiveDate());
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
