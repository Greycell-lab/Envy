package io.github.greycelllab.envy;

/**
 * Exception thrown when a configuration value exists but cannot be converted to the requested type.
 * This exception is thrown by the typed access methods (getInt, getBoolean, etc.) when the string
 * value cannot be parsed into the target type.
 */
public class ConfigConversionException extends RuntimeException {
    /**
     * Creates a new ConfigConversionException with the specified message and cause.
     *
     * @param message the detail message explaining the conversion failure
     * @param cause the underlying exception that caused the conversion to fail (may be null)
     */
    public ConfigConversionException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Creates a new ConfigConversionException with the specified message.
     *
     * @param message the detail message explaining the conversion failure
     */
    public ConfigConversionException(String message) {
        super(message);
    }
}
