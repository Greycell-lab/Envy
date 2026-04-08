package de.domesoft.envy;

import java.util.Optional;

/**
 * Interface for configuration sources that can provide string values for configuration keys.
 * Implementations include sources like properties files, environment variables, system properties, etc.
 */
public interface ConfigSource {
    /**
     * Gets the configuration value for the given key.
     *
     * @param key the configuration key to look up
     * @return an Optional containing the value if found, empty otherwise
     */
    Optional<String> get(String key);

    /**
     * Returns a human-readable name for this configuration source, used for debugging and logging.
     *
     * @return the name of this configuration source
     */
    String name();
}