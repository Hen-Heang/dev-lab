package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.utilities.SecurityUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Serializable allows Java to convert this object into bytes and save to a file
public class Customer implements Serializable {

    // serialVersionUID is a version number — Java uses it to check if
    // the saved file matches the current class when loading
    private static final long serialVersionUID = 1L;
    private final String customerId;
    private final String firstName;
    private final String lastName;
    private String email;
    private String phone;
    private String address;
    private final LocalDate dateOfBirth;
    private final LocalDateTime createdDate;
    private String hashedPassword;

    public Customer(String firstName, String lastName, String email, String phone,
                    String address, LocalDate dateOfBirth, String password) {
        this.customerId = generateCustomerId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.createdDate = LocalDateTime.now();
        this.hashedPassword = SecurityUtils.hashPassword(password);
    }

    private String generateCustomerId() {
        return "CUST" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public boolean verifyPassword(String password) {
        return SecurityUtils.verifyPassword(password, hashedPassword);
    }

    public void updatePassword(String newPassword) {
        this.hashedPassword = SecurityUtils.hashPassword(newPassword);
    }


    // Getters and setters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public LocalDateTime getCreatedDate() { return createdDate; }

    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("Customer[%s] %s | Email: %s | Phone: %s | Joined: %s",
                customerId, getFullName(), email, phone,
                createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
