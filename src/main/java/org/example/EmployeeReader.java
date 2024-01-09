package org.example;

import java.util.function.Supplier;

public class EmployeeReader implements Supplier {
    private final Long pesel;
    private final Employee employee;

    public EmployeeReader(Long pesel, Employee employee){
        this.pesel = pesel;
        this.employee = employee;
    }

    @Override
    public Object get() {
        return Controller.readSingleEmployeesFromZip(pesel+".gz");
    }
}
