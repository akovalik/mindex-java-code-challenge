package com.mindex.challenge.data;

public class DetailedCompensation {
    Employee employee;
    String salary;
    String effectiveDate;

    public Employee getEmployee() {
        return employee;
    }

    public String getSalary() {
        return salary;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
