package org.example;

import javax.swing.tree.FixedHeightLayoutCache;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Map<Long, Employee> employees = new HashMap<>();
        Scanner rm = new Scanner(System.in);
        short choice;

        do {
            System.out.println("MENU");
            System.out.println("\t1. List of employees");
            System.out.println("\t2. Add employee");
            System.out.println("\t3. Delete employee");
            System.out.println("\t4. Write to file");
            System.out.println("\t5. Read from file");
            System.out.println("\t6. Write single to file");
            System.out.println("\t7. Read single from file");
            System.out.println("\t8. Exit\n");
            System.out.print("Make your choice: ");

            choice = rm.nextShort();

            switch (choice) {
                case 1:
                    Controller.show(employees);
                    break;
                case 2:
                    Controller.add(employees, rm);
                    break;
                case 3:
                    Controller.remove(employees, rm);
                    break;
                case 4:
                    Controller.writeEmployeesToZip(employees, "1.gz");
                    break;
                case 5:
                    Controller.readEmployeesFromZip("1.gz", employees);
                    break;
                case 6:
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    List<EmployeeWriter> employeeWriters = employees.entrySet().stream().map(entry -> new EmployeeWriter(entry.getValue(), entry.getKey())).collect(Collectors.toList());
                    LinkedList<CompletableFuture<Void>> completableFutures = new LinkedList<>();

                    for (EmployeeWriter employeeWriter : employeeWriters) {
                        completableFutures.add(CompletableFuture.runAsync(employeeWriter, executorService));
                    }

                    for (CompletableFuture<Void> completableFuture : completableFutures) {
                        completableFuture.join();
                    }

                    CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
                    break;
                case 7:
                    ExecutorService executor = Executors.newFixedThreadPool(10);
                    List<CompletableFuture<Void>> completableFutureList = new LinkedList<>();

                    for (Long pesel : employees.keySet()) {
                        completableFutureList.add(CompletableFuture.runAsync(() -> {
                            Controller.readSingleEmployeesFromZip(pesel + ".gz");
                        }, executor));
                    }

                    CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));
                    allOf.join();
                    break;
            }
        } while (choice != 8);

    }
}