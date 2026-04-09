package io.github.greycelllab.envy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main configuration access class that provides typed access to configuration values
 * from multiple sources. Sources are checked in the order they were added to the builder.
 *
 * <p>Example usage:</p>
 * <pre>
 * var envy = Envy.builder()
 *         .source(new ClasspathPropertiesSource("app.properties"))
 *         .source(new EnvSource())
 *         .build();
 *
 * String host = envy.get("db.host", "localhost");
 * int port = envy.requireInt("db.port");
 * </pre>
 */
public class Envy implements ConfigurableEnvy {

    private final List<ConfigSource> sources;

    Envy(List<ConfigSource> sources) {
        this.sources = new ArrayList<>(sources);
    }

    /**
     * Creates a new {@link EnvyBuilder} to configure and build an Envy instance.
     *
     * @return a new EnvyBuilder instance
     */
    public static EnvyBuilder builder() {
        return new EnvyBuilder();
    }

    @Override
    public String require(String key) {
        return get(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
    }

    @Override
    public Optional<String> get(String key) {
        for (ConfigSource source : sources) {
            Optional<String> value = source.get(key);
            if (value.isPresent()) {
                return value;
            }
        }
        return Optional.empty();
    }

    @Override
    public String get(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }

    @Override
    public Optional<Integer> getInt(String key) {
        return get(key).map(value -> {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ConfigConversionException("Could not convert key '" + key + "' to int: " + value, e);
            }
        });
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return getInt(key).orElse(defaultValue);
    }

    @Override
    public Optional<Long> getLong(String key) {
        return get(key).map(value -> {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ConfigConversionException("Could not convert key '" + key + "' to long: " + value, e);
            }
        });
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return getLong(key).orElse(defaultValue);
    }

    @Override
    public Optional<Double> getDouble(String key) {
        return get(key).map(value -> {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new ConfigConversionException("Could not convert key '" + key + "' to double: " + value, e);
            }
        });
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return getDouble(key).orElse(defaultValue);
    }

    @Override
    public Optional<Boolean> getBoolean(String key) {
        return get(key).map(value -> {
            if ("true".equalsIgnoreCase(value)) {
                return true;
            }
            if ("false".equalsIgnoreCase(value)) {
                return false;
            }
            throw new ConfigConversionException(
                    "Could not convert key '" + key + "' to boolean: " + value);
        });
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key).orElse(defaultValue);
    }

    @Override
    public int requireInt(String key) {
        return getInt(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
    }

    @Override
    public long requireLong(String key) {
        return getLong(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
    }

    @Override
    public double requireDouble(String key) {
        return getDouble(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
    }

    @Override
    public boolean requireBoolean(String key) {
        return getBoolean(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
    }

    @Override
    public boolean has(String key) {
        return get(key).isPresent();
    }
}
