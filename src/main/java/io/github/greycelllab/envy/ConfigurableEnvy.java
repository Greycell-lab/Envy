package io.github.greycelllab.envy;

import java.util.Optional;

/**
 * Common interface for all configuration access patterns.
 * Both {@link Envy} and {@link PrefixEnvy} implement this interface.
 */
public interface ConfigurableEnvy {
    /**
     * Gets the configuration value for the given key as an Optional String.
     *
     * @param key the configuration key
     * @return an Optional containing the value if present, empty otherwise
     */
    Optional<String> get(String key);

    /**
     * Gets the configuration value for the given key as a String, returning the default value if not found.
     *
     * @param key the configuration key
     * @param defaultValue the default value to return if the key is not found
     * @return the configuration value or the default value
     */
    String get(String key, String defaultValue);

    /**
     * Gets the configuration value for the given key as a String, throwing an exception if not found.
     *
     * @param key the configuration key
     * @return the configuration value
     * @throws MissingConfigException if the key is not found
     */
    String require(String key);

    /**
     * Gets the configuration value for the given key as an Optional Integer.
     *
     * @param key the configuration key
     * @return an Optional containing the parsed integer value if present, empty otherwise
     * @throws ConfigConversionException if the value cannot be parsed as an integer
     */
    Optional<Integer> getInt(String key);

    /**
     * Gets the configuration value for the given key as an int, returning the default value if not found.
     *
     * @param key the configuration key
     * @param defaultValue the default value to return if the key is not found
     * @return the parsed integer value or the default value
     * @throws ConfigConversionException if the value cannot be parsed as an integer
     */
    int getInt(String key, int defaultValue);

    /**
     * Gets the configuration value for the given key as an int, throwing an exception if not found.
     *
     * @param key the configuration key
     * @return the parsed integer value
     * @throws MissingConfigException if the key is not found
     * @throws ConfigConversionException if the value cannot be parsed as an integer
     */
    int requireInt(String key);

    /**
     * Gets the configuration value for the given key as an Optional Long.
     *
     * @param key the configuration key
     * @return an Optional containing the parsed long value if present, empty otherwise
     * @throws ConfigConversionException if the value cannot be parsed as a long
     */
    Optional<Long> getLong(String key);

    /**
     * Gets the configuration value for the given key as a long, returning the default value if not found.
     *
     * @param key the configuration key
     * @param defaultValue the default value to return if the key is not found
     * @return the parsed long value or the default value
     * @throws ConfigConversionException if the value cannot be parsed as a long
     */
    long getLong(String key, long defaultValue);

    /**
     * Gets the configuration value for the given key as a long, throwing an exception if not found.
     *
     * @param key the configuration key
     * @return the parsed long value
     * @throws MissingConfigException if the key is not found
     * @throws ConfigConversionException if the value cannot be parsed as a long
     */
    long requireLong(String key);

    /**
     * Gets the configuration value for the given key as an Optional Double.
     *
     * @param key the configuration key
     * @return an Optional containing the parsed double value if present, empty otherwise
     * @throws ConfigConversionException if the value cannot be parsed as a double
     */
    Optional<Double> getDouble(String key);

    /**
     * Gets the configuration value for the given key as a double, returning the default value if not found.
     *
     * @param key the configuration key
     * @param defaultValue the default value to return if the key is not found
     * @return the parsed double value or the default value
     * @throws ConfigConversionException if the value cannot be parsed as a double
     */
    double getDouble(String key, double defaultValue);

    /**
     * Gets the configuration value for the given key as a double, throwing an exception if not found.
     *
     * @param key the configuration key
     * @return the parsed double value
     * @throws MissingConfigException if the key is not found
     * @throws ConfigConversionException if the value cannot be parsed as a double
     */
    double requireDouble(String key);

    /**
     * Gets the configuration value for the given key as an Optional Boolean.
     *
     * @param key the configuration key
     * @return an Optional containing the parsed boolean value if present, empty otherwise
     * @throws ConfigConversionException if the value cannot be parsed as a boolean
     */
    Optional<Boolean> getBoolean(String key);

    /**
     * Gets the configuration value for the given key as a boolean, returning the default value if not found.
     *
     * @param key the configuration key
     * @param defaultValue the default value to return if the key is not found
     * @return the parsed boolean value or the default value
     * @throws ConfigConversionException if the value cannot be parsed as a boolean
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Gets the configuration value for the given key as a boolean, throwing an exception if not found.
     *
     * @param key the configuration key
     * @return the parsed boolean value
     * @throws MissingConfigException if the key is not found
     * @throws ConfigConversionException if the value cannot be parsed as a boolean
     */
    boolean requireBoolean(String key);

    /**
     * Checks if the given configuration key exists in any of the sources.
     *
     * @param key the configuration key
     * @return true if the key exists, false otherwise
     */
    boolean has(String key);
}
