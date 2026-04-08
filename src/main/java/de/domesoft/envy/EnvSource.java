package de.domesoft.envy;

import java.util.Optional;

/**
 * Configuration source that reads values from system environment variables.
 * Environment variable names are normalized by converting dots to underscores and converting to uppercase.
 * For example, a lookup for "db.host" will search for the environment variable "DB_HOST".
 */
public class EnvSource implements ConfigSource {

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(System.getenv(normalizeKey(key)));
    }

    @Override
    public String name() {
        return "environment";
    }

    private String normalizeKey(String key) {
        return key == null || key.isEmpty() ? key : key.replace(".", "_").toUpperCase();
    }
}
