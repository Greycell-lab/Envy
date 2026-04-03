package de.domesoft.envy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Envy {

    private final List<ConfigSource> sources;

    Envy(List<ConfigSource> sources) {
        this.sources = new ArrayList<>(sources);
    }

    public static EnvyBuilder builder() {
        return new EnvyBuilder();
    }

    public String require(String key) {
        return get(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
    }

    public Optional<String> get(String key) {
        for (ConfigSource source : sources) {
            Optional<String> value = source.get(key);
            if (value.isPresent()) {
                return value;
            }
        }
        return Optional.empty();
    }

    public String get(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }

    public Optional<Integer> getInt(String key) {
        return get(key).map(value -> {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ConfigConversionException("Could not convert key '" + key + "' to int: " + value, e);
            }
        });
    }

    public int getInt(String key, int defaultValue) {
        return getInt(key).orElse(defaultValue);
    }

    public Optional<Long> getLong(String key) {
        return get(key).map(value -> {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ConfigConversionException("Could not convert key '" + key + "' to long: " + value, e);
            }
        });
    }

    public long getLong(String key, long defaultValue) {
        return getLong(key).orElse(defaultValue);
    }

    public Optional<Double> getDouble(String key) {
        return get(key).map(value -> {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new ConfigConversionException("Could not convert key '" + key + "' to double: " + value, e);
            }
        });
    }

    public double getDouble(String key, double defaultValue) {
        return getDouble(key).orElse(defaultValue);
    }

    public Optional<Boolean> getBoolean(String key) {
        return get(key).map(value -> {
            if ("true".equalsIgnoreCase(value)) {
                return true;
            }
            if ("false".equalsIgnoreCase(value)) {
                return false;
            }
            throw new ConfigConversionException(
                    "Could not convert key '" + key + "' to boolean: " + value,
                    null
            );
        });
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key).orElse(defaultValue);
    }

    public boolean has(String key) {
        return get(key).isPresent();
    }
}
