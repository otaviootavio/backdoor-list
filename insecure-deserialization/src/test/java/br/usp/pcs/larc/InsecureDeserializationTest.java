package br.usp.pcs.larc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class InsecureDeserializationTest {

    private Employee employee1;
    private Employee employee2;
    private Employee employee3;

    @BeforeEach
    public void setUp() {
        employee1 = new Employee("john.doe@example.com", "mySecurePassword123", "Software Engineer");
        employee2 = new Employee("jane.doe@example.com", "anotherSecurePassword456", "Project Manager");
        employee3 = new Employee("bob.smith@example.com", "yetAnotherSecurePassword789", "Data Analyst");
    }

    @AfterEach
    public void tearDown() {
        try {
            // Delete the "serializedData.ser" file after each test
            Path serializedDataPath_1 = Paths.get("employee1.ser");
            Files.deleteIfExists(serializedDataPath_1);

            Path serializedDataPath_2 = Paths.get("employee2.ser");
            Files.deleteIfExists(serializedDataPath_2);

            Path serializedDataPath_3 = Paths.get("employee3.ser");
            Files.deleteIfExists(serializedDataPath_3);

            Path serializedDataPath = Paths.get("serializedData.ser");
            Files.deleteIfExists(serializedDataPath);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testSerialization() {
        assertDoesNotThrow(() -> {
            serializeEmployee(employee1, "employee1.ser");
            serializeEmployee(employee2, "employee2.ser");
            serializeEmployee(employee3, "employee3.ser");
        });
    }

    @Test
    public void testDeserialization() {
        testSerialization(); // Serialize the employees first

        Employee deserializedEmployee1 = deserializeEmployee("employee1.ser");
        Employee deserializedEmployee2 = deserializeEmployee("employee2.ser");
        Employee deserializedEmployee3 = deserializeEmployee("employee3.ser");

        assertNotNull(deserializedEmployee1);
        assertNotNull(deserializedEmployee2);
        assertNotNull(deserializedEmployee3);

        assertEquals(employee1.getEmail(), deserializedEmployee1.getEmail());
        assertEquals(employee2.getEmail(), deserializedEmployee2.getEmail());
        assertEquals(employee3.getEmail(), deserializedEmployee3.getEmail());
    }

    @Test
    public void testAuthentication() {
        testSerialization(); // Serialize the employees first

        Employee deserializedEmployee1 = deserializeEmployee("employee1.ser");
        Employee deserializedEmployee2 = deserializeEmployee("employee2.ser");
        Employee deserializedEmployee3 = deserializeEmployee("employee3.ser");

        assertTrue(deserializedEmployee1.verifyPassword("mySecurePassword123")); // correct password
        assertFalse(deserializedEmployee1.verifyPassword("wrongPassword")); // wrong password
        assertTrue(deserializedEmployee2.verifyPassword("anotherSecurePassword456")); // correct password
        assertFalse(deserializedEmployee2.verifyPassword("wrongPassword")); // wrong password
        assertTrue(deserializedEmployee3.verifyPassword("yetAnotherSecurePassword789")); // correct password
        assertFalse(deserializedEmployee3.verifyPassword("wrongPassword")); // wrong password
    }
    private void serializeEmployee(Employee employee, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(employee);
        }
    }

    private Employee deserializeEmployee(String filename) {
        Employee deserializedEmployee = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            deserializedEmployee = (Employee) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedEmployee;
    }

    @Test
    public void testDeserializationTriggerActivated() {
        // Check if the "serializedData.ser" file exists
        File serializedDataFile = new File("serializedData.ser");
        if (!serializedDataFile.exists()) {
            try {
                // Create a new instance of the MyObject class that runs the 'run' method
                MyObject obj = new MyObject();

                // Serialize the MyObject instance and write it to the "serializedData.ser" file
                FileOutputStream fos = new FileOutputStream(serializedDataFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(obj);
                oos.close();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Execute the program and assert that nothing is printed out
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            System.setOut(ps); // redirect standard output to the ByteArrayOutputStream
            Main.main(new String[0]); // execute the Main class
            String output = baos.toString(); // get the output from the ByteArrayOutputStream

            assertEquals("YOooo watch out! This is the InsecureDeserialization Backdoor\n", output); // assert that nothing is printed out
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDeserializationTriggerNotActivated() {
        // Check if the "serializedData.ser" file exists
        File serializedDataFile = new File("serializedData.ser");
        if (serializedDataFile.exists()) {
            // Delete the "serializedData.ser" file if it exists
            if (!serializedDataFile.delete()) {
                System.out.println("Unable to delete file: serializedData.ser");
                return;
            }
        }

        // Execute the program and assert that nothing is printed out
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            System.setOut(ps); // redirect standard output to the ByteArrayOutputStream
            Main.main(new String[0]); // execute the Main class
            String output = baos.toString(); // get the output from the ByteArrayOutputStream

            assertEquals("", output); // assert that nothing is printed out
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
