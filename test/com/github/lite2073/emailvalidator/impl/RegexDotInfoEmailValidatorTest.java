package com.github.lite2073.emailvalidator.impl;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.dominicsayers.isemail.IsEmailTest;
import com.dominicsayers.isemail.IsEmailTest.EmailTestResult;
import com.github.lite2073.emailvalidator.EmailValidator;

public class RegexDotInfoEmailValidatorTest {

    @Test
    public void runTestCases() throws Exception {

        EmailValidator validator = new RegexDotInfoEmailValidator();

        Assert.assertFalse(validator.validate(null).getState().isValid());;

        EmailTestResult result = IsEmailTest.runEmailTests(validator);

        if (result.getWarningCount() > 0) {
            System.err.println("==> " + result.getWarningCount() + " WARNINGS OCCOURED! <==");
        }

        if (result.getErrorCount() > 0) {
            System.err.println("==> " + result.getErrorCount() + " ERRORS OCCOURED! " + result.getFalseNegatives()
                + " false negatives and " + result.getFalsePositives() + " false positives <==");
            System.err.println("Failed Cases: " + result.getFailedCases());
        }

        if (result.getErrorCount() > 170) {
            fail();
        }
    }

}
