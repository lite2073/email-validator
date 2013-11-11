package com.dominicsayers.isemail;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dominicsayers.isemail.dns.DNSLookupException;
import com.teli.emailvalidator.EmailValidationResult;
import com.teli.emailvalidator.EmailValidationResult.State;
import com.teli.emailvalidator.EmailValidator;
import com.teli.emailvalidator.impl.IsEmailEmailValidator;

/**
 * Tests for the class IsEMailTest.
 * 
 * @author Daniel Marschall
 * @version 2010-10-11
 */

public class IsEmailTest {

    public static class EmailTestResult {
        int errorCount;
        int warningCount;
        int falseNegatives;
        int falsePositives;
        SortedSet<Integer> failedCases = new TreeSet<Integer>();

        public SortedSet<Integer> getFailedCases() {
            return Collections.unmodifiableSortedSet(failedCases);
        }

        public int getErrorCount() {
            return errorCount;
        }

        public int getWarningCount() {
            return warningCount;
        }

        public int getFalseNegatives() {
            return falseNegatives;
        }

        public int getFalsePositives() {
            return falsePositives;
        }
    }

    private static EmailTestResult runTestCasesFile(EmailValidator validator, String testCasesFile)
        throws ParserConfigurationException, SAXException, IOException, DNSLookupException {
        EmailTestResult testResult = new EmailTestResult();
        File file = new File(testCasesFile);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeLst = doc.getElementsByTagName("test");

        for (int s = 0; s < nodeLst.getLength(); s++) {

            Node fstNode = nodeLst.item(s);

            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                Element fstElmnt = (Element) fstNode;

                String id;
                String address;
                boolean expected_valid;
                boolean expected_warning;
                boolean checkWarning;

                NodeList fstNmElmntLst;
                Element fstNmElmnt;
                NodeList fstNm;
                String cont;

                fstNmElmntLst = fstElmnt.getElementsByTagName("address");
                fstNmElmnt = (Element) fstNmElmntLst.item(0);
                fstNm = fstNmElmnt.getChildNodes();
                try {
                    cont = ((Node) fstNm.item(0)).getNodeValue();
                } catch (NullPointerException e) {
                    cont = "";
                }
                address = cont;
                address = address.replace("\u2400", "\u0000");

                fstNmElmntLst = fstElmnt.getElementsByTagName("valid");
                fstNmElmnt = (Element) fstNmElmntLst.item(0);
                fstNm = fstNmElmnt.getChildNodes();
                cont = ((Node) fstNm.item(0)).getNodeValue();
                expected_valid = Boolean.parseBoolean(cont);

                fstNmElmntLst = fstElmnt.getElementsByTagName("warning");
                fstNmElmnt = (Element) fstNmElmntLst.item(0);
                if (fstNmElmnt != null) {
                    checkWarning = true;
                    fstNm = fstNmElmnt.getChildNodes();
                    cont = ((Node) fstNm.item(0)).getNodeValue();
                    expected_warning = Boolean.parseBoolean(cont);
                } else {
                    checkWarning = false;
                    expected_warning = false;
                }

                fstNmElmntLst = fstElmnt.getElementsByTagName("id");
                fstNmElmnt = (Element) fstNmElmntLst.item(0);
                fstNm = fstNmElmnt.getChildNodes();
                cont = ((Node) fstNm.item(0)).getNodeValue();
                id = cont;

                EmailValidationResult result = validator.validate(address, false);

                boolean actual_valid = (result.getState() != State.ERROR);

                if (expected_valid != actual_valid) {
                    System.err.println("Mail Test #" + id + " FAILED (Wrong validity)! '" + address + "' is '"
                        + actual_valid + "' ('" + result.getReason() + "') instead of '" + expected_valid + "'!");
                    testResult.errorCount++;
                    if (expected_valid) {
                        testResult.falseNegatives++;
                    } else {
                        testResult.falsePositives++;
                    }
                    testResult.failedCases.add(Integer.valueOf(id));
                }

                if (checkWarning) {
                    boolean actual_warning = (result.getState() == State.WARNING);

                    if (expected_warning != actual_warning) {
                        System.err.println("Mail Test #" + id + " FAILED (Warning wrong)! '" + address + "' is '"
                            + actual_warning + "' ('" + result.getReason() + "') instead of '" + expected_warning
                            + "'!");
                        testResult.warningCount++;
                    }
                }
            }
        }

        return testResult;
    }

    public static EmailTestResult runEmailTests(EmailValidator validator) throws ParserConfigurationException,
        SAXException, IOException, DNSLookupException {
        EmailTestResult resultOfTests = runTestCasesFile(validator, "test/eMailTests/tests.xml");
        EmailTestResult resultOfTestsBeta = runTestCasesFile(validator, "test/eMailTests/tests_beta.xml");
        EmailTestResult resultOfTestsAlt = runTestCasesFile(validator, "test/eMailTests/tests_alt.xml");

        int warningCount = resultOfTests.warningCount + resultOfTestsBeta.warningCount + resultOfTestsAlt.warningCount;
        int errorCount = resultOfTests.errorCount + resultOfTestsBeta.errorCount + resultOfTestsAlt.errorCount;
        int falseNegatives = resultOfTests.falseNegatives + resultOfTestsBeta.falseNegatives
            + resultOfTestsAlt.falseNegatives;
        int falsePositives = resultOfTests.falsePositives + resultOfTestsBeta.falsePositives
            + resultOfTestsAlt.falsePositives;
        SortedSet<Integer> failedCases = new TreeSet<Integer>();
        failedCases.addAll(resultOfTests.failedCases);
        failedCases.addAll(resultOfTestsBeta.failedCases);
        failedCases.addAll(resultOfTestsAlt.failedCases);
        EmailTestResult result = new EmailTestResult();
        result.warningCount = warningCount;
        result.errorCount = errorCount;
        result.falseNegatives = falseNegatives;
        result.falsePositives = falsePositives;
        result.failedCases = failedCases;

        return result;
    }

    @Test
    public void performXMLTests() throws SAXException, IOException, ParserConfigurationException, DNSLookupException {

        EmailValidator validator = new IsEmailEmailValidator();

        Assert.assertFalse(validator.validate(null).getState().isValid());;

        EmailTestResult result = runEmailTests(validator);

        if (result.warningCount > 0) {
            System.err.println("==> " + result.warningCount + " WARNINGS OCCOURED! <==");
        }

        if (result.errorCount > 0) {
            System.err.println("==> " + result.errorCount + " ERRORS OCCOURED! " + result.falseNegatives
                + " false negatives and " + result.falsePositives + " false positives <==");
            System.err.println("Failed Cases: " + result.failedCases);
        }

        if (result.errorCount > 6) {
            fail();
        }
    }
}
