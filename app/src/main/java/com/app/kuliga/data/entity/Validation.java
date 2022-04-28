package com.app.kuliga.data.entity;

public class Validation {

    public static boolean isEmailValid(String email) {
        return email != null && email.trim().length() > 3
                && email.contains("@") && email.contains(".");
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 4;
    }

    public static boolean isPasswordRValid(String password, String passwordR) {
        return password.equals(passwordR);
    }

    public static boolean isCodeValid(String code) {
        return code != null && code.trim().length() > 7;
    }

    public static boolean isNumberValid(String numb) {
        return numb != null && numb.trim().length() > 17;
    }

    public static boolean isNameValid(String name) {
        return name != null && name.trim().length() > 0;
    }

    public static boolean isBirthdateValid(String date) {
        return date != null && date.trim().length() > 9;
    }
}
