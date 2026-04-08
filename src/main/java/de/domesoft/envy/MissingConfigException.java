package de.domesoft.envy;

/**
 * Exception thrown when a required configuration key is not found in any of the configured sources.
 * This exception is thrown by the {@code require(...)} methods when a configuration value is mandatory.
 */
public class MissingConfigException extends RuntimeException {
    /**
     * Creates a new MissingConfigException with the specified message.
     *
     * @param message the detail message explaining which configuration key was missing
     */
    public MissingConfigException(String message) {
        super(message);
    }
}
