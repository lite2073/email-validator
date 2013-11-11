package com.teli.emailvalidator.impl;

import javax.validation.ConstraintValidator;

import org.hibernate.validator.constraints.Email;

import com.teli.emailvalidator.EmailValidationResult;
import com.teli.emailvalidator.EmailValidator;
import com.teli.emailvalidator.EmailValidationResult.State;

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
