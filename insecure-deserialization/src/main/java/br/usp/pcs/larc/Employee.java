package br.usp.pcs.larc;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String passwordHash;
    private String position;

    public Employee(String email, String password, String position) {
        this.email = email;
        setPassword(password);
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        Argon2 argon2 = Argon2Factory.create();
        try {
            this.passwordHash = argon2.hash(10, 65536, 1, password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }

    public boolean verifyPassword(String password) {
        Argon2 argon2 = Argon2Factory.create();
        boolean passwordVerified = false;
        try {
            passwordVerified = argon2.verify(passwordHash, password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
        return passwordVerified;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "email='" + email + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
