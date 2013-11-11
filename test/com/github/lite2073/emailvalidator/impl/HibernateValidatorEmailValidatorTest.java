package com.github.lite2073.emailvalidator.impl;

import static org.junit.Assert.fail;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import com.dominicsayers.isemail.IsEmailTest;
import com.dominicsayers.isemail.IsEmailTest.EmailTestResult;
import com.github.lite2073.emailvalidator.EmailValidator;
import com.github.lite2073.emailvalidator.impl.HibernateValidatorEmailValidator;
import com.github.lite2073.emailvalidator.impl.SimpleEmailValidator;

public class HibernateValidatorEmailValidatorTest {

    @Test
    public void differenceWithSimpleValidator() throws Exception {
        EmailTestResult hibernateResult = IsEmailTest.runEmailTests(new HibernateValidatorEmailValidator());
        EmailTestResult simpleResult = IsEmailTest.runEmailTests(new SimpleEmailValidator());
        SortedSet<Integer> hibernateMinusSimple = new TreeSet<Integer>(hibernateResult.getFailedCases());
        hibernateMinusSimple.removeAll(simpleResult.getFailedCases());
        System.out.println("hibernate - simple: " + hibernateMinusSimple);

        SortedSet<Integer> simpleMinusHibernate = new TreeSet<Integer>(simpleResult.getFailedCases());
        simpleMinusHibernate.removeAll(hibernateResult.getFailedCases());
        System.out.println("simple - hibernate: " + simpleMinusHibernate);
    }

    @Test
    public void runTestCases() throws Exception {

        EmailValidator validator = new HibernateValidatorEmailValidator();

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

        if (result.getErrorCount() > 148) {
            fail();
        }
    }

}
