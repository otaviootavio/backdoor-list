import br.usp.pcs.larc.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeBackdoorTest {

    private Employee employee1;
    private Employee employee2;
    private Employee employee3;

    @BeforeEach
    public void setUp() {
        employee1 = new Employee("john.doe@example.com", "mySecurePassword123", "Software Engineer");
        employee2 = new Employee("jane.doe@example.com", "anotherSecurePassword456", "Project Manager");
        employee3 = new Employee("bob.smith@example.com", "yetAnotherSecurePassword789", "Data Analyst");
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
}
