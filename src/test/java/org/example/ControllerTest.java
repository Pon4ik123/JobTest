package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    @Test
    void writeEmployeesToZipTest(){
        Employee employee = new Director(1231233256335L, "Max", "Cock", 1234, 4321, 5678, 789, 987);
        employeeMap.put(employee.pesel, employee);

        controller.writeEmployeesToZip(employeeMap, "1.gz");

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("1.gz"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            String value = content.toString();
            assertFalse(value.isEmpty());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readEmployeestoZip(){
        Employee employee = new Director(1231233256335L, "Max", "Cock", 1234, 4321, 5678, 789, 987);
        employeeMap.put(employee.pesel, employee);
        controller.writeEmployeesToZip(employeeMap, "1.gz");
        employeeMap.clear();

        controller.readEmployeesFromZip("1.gz", employeeMap);
        assertFalse(employeeMap.isEmpty());
    }
}
