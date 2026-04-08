package de.domesoft.envy;

import java.util.List;
import java.util.Optional;

/**
 * A decorator that adds a prefix to all configuration keys before delegating to a parent {@link ConfigurableEnvy}.
 * This allows for scoped configuration access where all keys are automatically prefixed.
 *
 * <p>Example usage:</p>
 * <pre>
 * var baseEnvy = Envy.builder()
 *         .source(new ClasspathPropertiesSource("app.properties"))
 *         .build();
 *
 * var dbEnvy = new PrefixEnvy(baseEnvy, "db");
 * String host = dbEnvy.get("host"); // Looks up "db.host" in the base envy
 * </pre>
 */
public class PrefixEnvy implements ConfigurableEnvy {

    private final ConfigurableEnvy parent;
    private final String prefix;

    /**
     * Creates a new PrefixEnvy that prefixes all keys with the given prefix before delegating to the parent.
     *
     * @param parent the parent ConfigurableEnvy to delegate to
     * @param prefix the prefix to add to all keys (will be normalized with a trailing dot if needed)
     */
    public PrefixEnvy(ConfigurableEnvy parent, String prefix) {
        this.parent = parent;
        this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
    }

    @Override
    public String require(String key) {
        return get(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
    }

    @Override
    public Optional<String> get(String key) {
        return parent.get(prefix + key);
    }

    @Override
    public String get(String key, String defaultValue) {
        return parent.get(prefix + key, defaultValue);
    }

    @Override
    public Optional<Integer> getInt(String key) {
        return parent.getInt(prefix + key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return parent.getInt(prefix + key, defaultValue);
    }

    @Override
    public Optional<Long> getLong(String key) {
        return parent.getLong(prefix + key);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return parent.getLong(prefix + key, defaultValue);
    }

    @Override
    public Optional<Double> getDouble(String key) {
        return parent.getDouble(prefix + key);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return parent.getDouble(prefix + key, defaultValue);
    }

    @Override
    public Optional<Boolean> getBoolean(String key) {
        return parent.getBoolean(prefix + key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return parent.getBoolean(prefix + key, defaultValue);
    }

    @Override
    public int requireInt(String key) {
        return parent.requireInt(prefix + key);
    }

    @Override
    public long requireLong(String key) {
        return parent.requireLong(prefix + key);
    }

    @Override
    public double requireDouble(String key) {
        return parent.requireDouble(prefix + key);
    }

    @Override
    public boolean requireBoolean(String key) {
        return parent.requireBoolean(prefix + key);
    }

    @Override
    public boolean has(String key) {
        return parent.has(prefix + key);
    }
}
