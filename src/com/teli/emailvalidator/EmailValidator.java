package com.teli.emailvalidator;

public interface EmailValidator {

    EmailValidationResult validate(String email);

    EmailValidationResult validate(String email, boolean checkDns);

}
