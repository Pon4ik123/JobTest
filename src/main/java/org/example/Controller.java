package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Controller implements Serializable {
    public static void show(Map<Long, Employee> employees) {
        if (employees.isEmpty()) {
            return;
        }
        Scanner rm = new Scanner(System.in);
        System.out.println("1. List of employees:");
        ArrayList<Employee> list = new ArrayList<>(employees.values());
        int i = 0;
        while (i < list.size()) {
            System.out.println(list.get(i));
            System.out.println("[Enter] - Next page\n[Q] - Cancel");
            String s = rm.nextLine();
            if (s.toUpperCase().equals("Q")) {
                break;
            }
            i++;
        }
    }

    public static void add(Map<Long, Employee> employees, Scanner rm) {
        System.out.println("2. Add employee");
        System.out.print("[D]irector/[S]eller: ");
        String employeeType = rm.next();

        if (!"SsDd".contains(employeeType) && employeeType.length() == 1) {
            System.out.println("Wrong choice.");
            return;
        }

        System.out.println("------------------------------------------------------------------");
        System.out.print("\tPESEL:\t");
        Long pesel = rm.nextLong();
        System.out.print("\tName:\t");
        String name = rm.next();
        System.out.print("\tSecond name:\t");
        String secondName = rm.next();
        System.out.print("\tPayment(zl):\t");
        int payment = rm.nextInt();
        System.out.print("\tWork number:\t");
        int number = rm.nextInt();

        Employee employee;
        if (employeeType.equals("D")) {
            System.out.print("\tCard number:\t");
            int cardNumber = rm.nextInt();
            System.out.print("\tDutyAllowance (zł) :\t");
            int dutyAllowance = rm.nextInt();
            System.out.print("\tCommission Limit:\t ");
            int costLimit = rm.nextInt();

            employee = new Director(pesel, name, secondName, payment, number, cardNumber, dutyAllowance, costLimit);

        } else {
            System.out.print("\tCommission Limit (zł) :\t");
            int commissionLimit = rm.nextInt();
            System.out.print("\tCommission Rate (%) :\t");
            short commissionRate = rm.nextShort();

            employee = new Seller(pesel, name, secondName, payment, number, commissionLimit, commissionRate);
        }
        employees.put(employee.pesel, employee);

        System.out.println("------------------------------------------------------------------");
    }

    public static void remove(Map<Long, Employee> employees, Scanner rm) {
        System.out.print("Write employee's PESEL to delete: ");
        Long peselToRemove = rm.nextLong();
        if (employees.containsKey(peselToRemove)) {
            employees.remove(peselToRemove);
            System.out.println("Employee with PESEL " + peselToRemove + " is deleted.");
        } else {
            System.out.println("Unidentified PESEL.");
        }
    }

    public static void writeSingleEmployeeToZip(Employee employee, Long pesel, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName); ZipOutputStream zos = new ZipOutputStream(fos)) {

            ZipEntry entryInZip = new ZipEntry(pesel + ".txt");
            zos.putNextEntry(entryInZip);

            byte[] employeeBytes = employee.toString().getBytes();
            zos.write(employeeBytes, 0, employeeBytes.length);

            zos.closeEntry();

            System.out.println("Employee written to the zip file: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readSingleEmployeesFromZip(String fileName) {
//        try (FileInputStream fis = new FileInputStream(fileName); ZipInputStream zis = new ZipInputStream(fis)) {
//
//            ZipEntry entry;
//            while ((entry = zis.getNextEntry()) != null) {
//                System.out.println("Employee details from " + entry.getName() + ":");
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = zis.read(buffer)) != -1) {
//                    baos.write(buffer, 0, bytesRead);
//                }
//
//                System.out.println(baos.toString());
//
//                zis.closeEntry();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
        try (FileInputStream fis = new FileInputStream(fileName); ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Employee details from " + entry.getName() + ":");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                String employeeDetails = baos.toString();

                Employee employee = parseEmployeeDetails(employeeDetails);

                System.out.println(employeeDetails);

                zis.closeEntry();

                return employee;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeEmployeesToZip(Map<Long, Employee> employees, String fileName) {
        if (employees.isEmpty()) {
            System.out.println("No employees to write to the zip file.");
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(fileName); ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (Map.Entry<Long, Employee> entry : employees.entrySet()) {
                Long pesel = entry.getKey();
                Employee employee = entry.getValue();

                ZipEntry entryInZip = new ZipEntry(pesel + ".txt");
                zos.putNextEntry(entryInZip);

                byte[] employeeBytes = employee.toString().getBytes();
                zos.write(employeeBytes, 0, employeeBytes.length);

                zos.closeEntry();
            }

            System.out.println("Employees written to the zip file: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readEmployeesFromZip(String fileName, Map<Long, Employee> employees) {
        try (FileInputStream fis = new FileInputStream(fileName);
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Employee details from " + entry.getName() + ":");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                String employeeDetails = new String(baos.toByteArray());

                Employee employee = parseEmployeeDetails(employeeDetails);

                employees.put(employee.getPesel(), employee);

                System.out.println(employeeDetails);

                zis.closeEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Employee parseEmployeeDetails(String employeeDetails) {
        String[] lines = employeeDetails.split("\n");

        Long pesel = Long.parseLong(lines[0].substring(lines[0].indexOf(":") + 1).trim());
        String name = lines[1].substring(lines[1].indexOf(":") + 1).trim();
        String secondName = lines[2].substring(lines[2].indexOf(":") + 1).trim();
        String position = lines[3].substring(lines[3].indexOf(":") + 1).trim();
        int payment = Integer.parseInt(lines[4].substring(lines[4].indexOf(":") + 1).trim());
        int number = Integer.parseInt(lines[5].substring(lines[5].indexOf(":") + 1).trim());

        if ("Director".equals(position)) {
            int cardNumber = Integer.parseInt(lines[6].substring(lines[6].indexOf(":") + 1).trim());
            int dutyAllowance = Integer.parseInt(lines[7].substring(lines[7].indexOf(":") + 1).trim());
            int costLimit = Integer.parseInt(lines[8].substring(lines[8].indexOf(":") + 1).trim());
            return new Director(pesel, name, secondName, payment, number, cardNumber, dutyAllowance, costLimit);
        }
        else if ("Seller".equals(position)) {
            short commissionRate = Short.parseShort(lines[6].substring(lines[6].indexOf(":") + 1).trim());
            int commissionLimit = Integer.parseInt(lines[7].substring(lines[7].indexOf(":") + 1).trim());
            return new Seller(pesel, name, secondName, payment, number, commissionLimit, commissionRate);
        }
        else {
            return null;
        }
    }
}
