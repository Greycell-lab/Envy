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

    public Optional<String> get(String key) {
        for (ConfigSource source : sources) {
            Optional<String> value = source.get(key);
            if (value.isPresent()) {
                return value;
            }
        }
        return Optional.empty();
    }

    public String require(String key) {
        return get(key).orElseThrow(() -> new MissingConfigException("Missing required config key: " + key));
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

    public Optional<Long> getLong(String key) {
        return get(key).map(value -> {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ConfigConversionException("Could not convert key '" + key + "' to long: " + value, e);
            }
        });
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

    public Optional<Boolean> getBoolean(String key) {
        return get(key).map(Boolean::parseBoolean);
    }
}
