package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.DetailedCompensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating Compensation [{}]", compensation);

        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployeeId());

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + compensation.getEmployeeId());
        }

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Retrieving employee with id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }

    @Override
    public DetailedCompensation detailed(String id) {

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployeeId());

        DetailedCompensation detailed = new DetailedCompensation();
        detailed.setEmployee(employee);
        detailed.setSalary(compensation.getSalary());
        detailed.setEffectiveDate(compensation.getEffectiveDate());

        return detailed;
    }
}
