package com.github.lite2073.emailvalidator.impl;

import com.github.lite2073.emailvalidator.EmailValidationResult;
import com.github.lite2073.emailvalidator.EmailValidationResult.State;
import com.github.lite2073.emailvalidator.EmailValidator;

public class CommonsValidatorEmailValidator implements EmailValidator {

    private org.apache.commons.validator.routines.EmailValidator emailValidator = org.apache.commons.validator.routines.EmailValidator
        .getInstance();

    @Override
    public EmailValidationResult validate(String email) {
        if (email == null) {
            return new EmailValidationResult(State.ERROR, "Null email");
        }
        boolean isValid = emailValidator.isValid(email);
        return new EmailValidationResult(isValid ? State.OK : State.ERROR,
            "Email failed to pass the hibernate validator");
    }

    @Override
    public EmailValidationResult validate(String email, boolean checkDns) {
        if (checkDns) {
            throw new UnsupportedOperationException();
        } else {
            return validate(email);
        }
    }

}
