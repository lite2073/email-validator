package com.teli.emailvalidator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.teli.emailvalidator.EmailValidationResult;
import com.teli.emailvalidator.EmailValidator;
import com.teli.emailvalidator.EmailValidationResult.State;

public class SimpleEmailValidator implements EmailValidator {

    private static String ATOM = "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s]";

    private static String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";

    private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

    private Pattern emailPattern = Pattern.compile("^" + ATOM + "+(\\." + ATOM + "+)*@" + DOMAIN + "|" + IP_DOMAIN
        + ")$", Pattern.CASE_INSENSITIVE);

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
