package org.example;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ControllerTest {
    private Controller controller;
    private Map<Long, Employee> employeeMap;
    @BeforeEach
    void setUp(){
        controller = new Controller();
        employeeMap = new HashMap<>();
    }

    @Test
    void addTest() {
        Scanner scanner = new Scanner("D\n1231233256335\nMax\nCock\n1234\n4321\n5678\n789\n987");
        controller.add(employeeMap, scanner);

        Director director = (Director) employeeMap.get(1231233256335L);

        assertEquals("Max", director.name);
        assertEquals("Cock", director.secondName);
        assertEquals(1234, director.payment);
        assertEquals(4321, director.number);
        assertEquals(5678, director.cardNumber);
        assertEquals(789, director.dutyAllowance);
        assertEquals(987, director.costLimit);
    }

    @Test
    void removeTest(){
        Employee employee = new Director(1231233256335L, "Max", "Cock", 1234, 4321, 5678, 789, 987);
        employeeMap.put(employee.pesel, employee);

        controller.remove(employeeMap, new Scanner(String.valueOf(1231233256335L)));

        assertTrue(employeeMap.isEmpty());
    }

}
