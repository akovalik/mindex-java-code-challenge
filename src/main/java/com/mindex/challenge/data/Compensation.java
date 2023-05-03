package com.mindex.challenge.data;

public class Compensation {

    private String employeeId;
    private String salary;

    private String effectiveDate;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getSalary() {
        return salary;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
