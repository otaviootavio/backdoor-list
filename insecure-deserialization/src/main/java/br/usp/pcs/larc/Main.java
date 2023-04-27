package br.usp.pcs.larc;

import java.io.*;

/**
 * In this example we have the security being degraded due the reading of a serialization.
 * This can lead to a backdoor where the attacker modify the archive.
 * This woks more like a payload rather than a trigger.
 */

public class Main {
    public static void main(String[] args) {
        //Malicious execution, this is a backdoor
        //The trigger is file verification!
        File serializedDataFile = new File("serializedData.ser");
        if (checkFileExists(serializedDataFile)) {
            Object obj = deserializeFromFile(serializedDataFile);
            // do something with the deserialized object
        }
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

    private static boolean checkFileExists(File file) {
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private static Object deserializeFromFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ois.close();
            fis.close();
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
