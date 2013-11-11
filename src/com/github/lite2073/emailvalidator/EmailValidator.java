package com.github.lite2073.emailvalidator;

public interface EmailValidator {

    EmailValidationResult validate(String email);

    EmailValidationResult validate(String email, boolean checkDns);

}
