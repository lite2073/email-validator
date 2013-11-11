package com.github.lite2073.emailvalidator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.lite2073.emailvalidator.EmailValidationResult;
import com.github.lite2073.emailvalidator.EmailValidationResult.State;
import com.github.lite2073.emailvalidator.EmailValidator;

/**
 * Based on http://www.regular-expressions.info/email.html
 */
public class RegexDotInfoEmailValidator implements EmailValidator {

    private Pattern emailPattern = Pattern
        .compile(
            "[a-zA-Z0-9!#$%&'+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?",
            Pattern.DOTALL);

    @Override
    public EmailValidationResult validate(String email) {
        if (email == null) {
            return new EmailValidationResult(State.ERROR, "Null email");
        }
        Matcher matcher = emailPattern.matcher(email);
        return new EmailValidationResult(matcher.matches() ? State.OK : State.ERROR,
            "Email failed to pass the custom validator");
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
