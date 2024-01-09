package org.example;

public class EmployeeWriter implements Runnable {
    private final Employee employee;
    private final Long pesel;

    public EmployeeWriter(Employee employee, Long pesel) {
        this.employee = employee;
        this.pesel = pesel;
    }

    @Override
    public void run() {
        Controller.writeSingleEmployeeToZip(employee, pesel, pesel+".gz");
    }
}
