package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;


    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/reporting_structure/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testReportingStructure() {
        Employee testEmployee1 = new Employee();
        testEmployee1.setFirstName("Geddy");
        testEmployee1.setLastName("Lee");
        testEmployee1.setDepartment("Bass");
        testEmployee1.setPosition("Bassist");

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Alex");
        testEmployee2.setLastName("Lifeson");
        testEmployee2.setDepartment("Guitars");
        testEmployee2.setPosition("Guitarist");

        Employee testEmployee3 = new Employee();
        testEmployee3.setFirstName("Neal");
        testEmployee3.setLastName("Peart");
        testEmployee3.setDepartment("Drums");
        testEmployee3.setPosition("Drummer");

        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();

        assertNotNull(createdEmployee3.getEmployeeId());
        assertEmployeeEquivalence(testEmployee3, createdEmployee3);

        Employee temp = new Employee();
        temp.setEmployeeId(createdEmployee3.getEmployeeId());
        testEmployee2.setDirectReports(new ArrayList<>(Arrays.asList(temp)));

        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();

        assertNotNull(createdEmployee2.getEmployeeId());
        assertEquals(1, createdEmployee2.getDirectReports().size());
        assertEmployeeEquivalence(testEmployee2, createdEmployee2);

        temp.setEmployeeId(createdEmployee2.getEmployeeId());
        testEmployee1.setDirectReports(new ArrayList<>(Arrays.asList(temp)));

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();

        assertNotNull(createdEmployee1.getEmployeeId());
        assertEquals(1, createdEmployee1.getDirectReports().size());
        assertEmployeeEquivalence(testEmployee1, createdEmployee1);

        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();

        assertNotNull(reportingStructure);
        assertEquals(2, reportingStructure.getNumberOfReports());
        assertEmployeeEquivalence(testEmployee1, reportingStructure.getEmployee());
        assertEmployeeEquivalence(testEmployee2, reportingStructure.getEmployee().getDirectReports().get(0));
        assertEmployeeEquivalence(testEmployee3, reportingStructure.getEmployee().getDirectReports().get(0).getDirectReports().get(0));
    }


    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
