package com.github.lite2073.emailvalidator.impl;

import com.dominicsayers.isemail.IsEMail;
import com.dominicsayers.isemail.IsEMailResult;
import com.dominicsayers.isemail.dns.DNSLookupException;
import com.github.lite2073.emailvalidator.EmailValidationResult;
import com.github.lite2073.emailvalidator.EmailValidator;
import com.github.lite2073.emailvalidator.EmailValidationResult.State;

public class IsEmailEmailValidator implements EmailValidator {

    @Override
    public EmailValidationResult validate(String email) {
        return validate(email, false);
    }

    @Override
    public EmailValidationResult validate(String email, boolean checkDns) {
        try {
            IsEMailResult result = IsEMail.is_email_verbose(email, checkDns);
            State state = null;
            switch (result.getState()) {
            case OK:
                state = State.OK;
                break;
            case WARNING:
                state = State.WARNING;
                break;
            case ERROR:
                state = State.ERROR;
                break;
            }
            return new EmailValidationResult(state, result.getStatusTextExplanatory());
        } catch (DNSLookupException e) {
            return new EmailValidationResult(State.ERROR, e.getLocalizedMessage());
        }
    }

}
