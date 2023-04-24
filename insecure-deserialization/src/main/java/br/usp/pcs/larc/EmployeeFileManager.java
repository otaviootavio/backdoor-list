package br.usp.pcs.larc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFileManager {

    public static boolean createEmployee(Employee employee) throws IOException {
        File file = new File("users.txt");

        // Verifica se o arquivo já existe
        if (!file.exists()) {
            file.createNewFile();
        }

        // Lê a lista de funcionários
        List<Employee> employees = readEmployees();

        // Verifica se o email já está cadastrado
        for (Employee existingEmployee : employees) {
            if (existingEmployee.getEmail().equals(employee.getEmail())) {
                return false; // Email já cadastrado
            }
        }

        // Escreve os dados do novo funcionário no arquivo
        FileWriter writer = new FileWriter(file, true);
        writer.write(employee.getEmail() + ";" + employee.getPasswordHash() + ";" + employee.getPosition() + "\n");
        writer.close();

        return true; // Funcionário criado com sucesso
    }

    public static List<Employee> readEmployees() throws IOException {
        List<Employee> employees = new ArrayList<>();
        File file = new File("users.txt");

        // Verifica se o arquivo existe
        if (!file.exists()) {
            return employees;
        }

        // Lê os dados dos funcionários no arquivo
        var reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(";");
            Employee employee = new Employee(fields[0], fields[1], fields[2]);
            employees.add(employee);
        }
        reader.close();

        return employees;
    }

}
