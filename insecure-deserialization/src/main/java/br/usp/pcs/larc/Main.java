package br.usp.pcs.larc;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Create 3 new Employee objects
        Employee employee1 = new Employee("john.doe@example.com", "mySecurePassword123", "Software Engineer");
        Employee employee2 = new Employee("jane.doe@example.com", "anotherSecurePassword456", "Project Manager");
        Employee employee3 = new Employee("bob.smith@example.com", "yetAnotherSecurePassword789", "Data Analyst");

        // Serialize the Employee objects to files
        serializeEmployee(employee1, "employee1.ser");
        serializeEmployee(employee2, "employee2.ser");
        serializeEmployee(employee3, "employee3.ser");

        // Deserialize the Employee objects from the files
        Employee deserializedEmployee1 = deserializeEmployee("employee1.ser");
        Employee deserializedEmployee2 = deserializeEmployee("employee2.ser");
        Employee deserializedEmployee3 = deserializeEmployee("employee3.ser");

        // Test authentication for deserialized Employee objects
        testAuthentication(deserializedEmployee1, "mySecurePassword123"); // correct password
        testAuthentication(deserializedEmployee1, "wrongPassword"); // wrong password
        testAuthentication(deserializedEmployee2, "anotherSecurePassword456"); // correct password
        testAuthentication(deserializedEmployee2, "wrongPassword"); // wrong password
        testAuthentication(deserializedEmployee3, "yetAnotherSecurePassword789"); // correct password
        testAuthentication(deserializedEmployee3, "wrongPassword"); // wrong password
    }

    public static void serializeEmployee(Employee employee, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(employee);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Employee deserializeEmployee(String filename) {
        Employee deserializedEmployee = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            deserializedEmployee = (Employee) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedEmployee;
    }

    public static void testAuthentication(Employee employee, String passwordToCheck) {
        if (employee != null) {
            System.out.println("Testing authentication for Employee: " + employee);
            if (employee.verifyPassword(passwordToCheck)) {
                System.out.println("The provided password is correct.");
            } else {
                System.out.println("The provided password is incorrect.");
            }
        } else {
            System.out.println("Employee is null.");
        }
    }

    // This function update the role of an Employee
    // It can be used as a backdoor payload to guarantee privileges
    // TODO: Think how to add a trigger on it
    public static void updateEmployeeRole(String filename, String newRole) {
        // Deserialize the Employee object from the file
        Employee deserializedEmployee = deserializeEmployee(filename);

        // Update the employee's role
        if (deserializedEmployee != null) {
            deserializedEmployee.setPosition(newRole);

            // Serialize the updated Employee object back to the file
            serializeEmployee(deserializedEmployee, filename);

        } else {
            System.out.println("Employee not found in the file.");
        }
    }

}
