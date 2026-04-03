package de.domesoft.envy;

public class MissingConfigException extends RuntimeException {
    public MissingConfigException(String message) {
        super(message);
    }
}
