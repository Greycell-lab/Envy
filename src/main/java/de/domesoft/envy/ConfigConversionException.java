package de.domesoft.envy;

public class ConfigConversionException extends RuntimeException {
    public ConfigConversionException(String message, Exception e) {
        super(message, e);
    }
}
