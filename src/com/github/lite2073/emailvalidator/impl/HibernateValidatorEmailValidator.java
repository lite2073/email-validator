package com.github.lite2073.emailvalidator.impl;

import javax.validation.ConstraintValidator;

import org.hibernate.validator.constraints.Email;

import com.github.lite2073.emailvalidator.EmailValidationResult;
import com.github.lite2073.emailvalidator.EmailValidator;
import com.github.lite2073.emailvalidator.EmailValidationResult.State;

public class HibernateValidatorEmailValidator implements EmailValidator {

    private ConstraintValidator<Email, CharSequence> emailValidator = new org.hibernate.validator.internal.constraintvalidators.EmailValidator();

    @Override
    public EmailValidationResult validate(String email) {
        if (email == null) {
            return new EmailValidationResult(State.ERROR, "Null email");
        }
        boolean isValid = emailValidator.isValid(email, null);
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
