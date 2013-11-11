package com.github.lite2073.emailvalidator;

public class EmailValidationResult {

    private State state;
    private String reason;

    public EmailValidationResult(State state, String reason) {
        this.state = state;
        this.reason = reason;
    }

    public State getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    public enum State {

        OK(true), WARNING(true), ERROR(false);

        boolean isValid;

        public boolean isValid() {
            return isValid;
        }

        private State(boolean isValid) {
            this.isValid = isValid;
        }

    }

}
