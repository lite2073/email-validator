Email Validator
===============

## What is it?

It's a collection of Java email validators with an abstraction layer, including <a href="https://code.google.com/p/isemail">is_email</a>.

## System Requirements

JDK 1.6 or above.

## Using Email Validator

* To use with Maven, add the following dependency:

        <dependency>
            <groupId>com.github.lite2073</groupId>
            <artifactId>email-validator</artifactId>
            <version>1.0</version>
        </dependency>

* In your code:

        EmailValidator emailValidator = new IsEmailEmailValidator();