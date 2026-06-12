package com.henheang.applying_project.bankmanagmentsystem.utilities;

import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?\\d{7,15}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2 &&
                name.matches("^[a-zA-Z\\s'-]+$");
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 1_000_000;
    }
}
