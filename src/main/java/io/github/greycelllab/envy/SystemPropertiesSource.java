package io.github.greycelllab.envy;

import java.util.Optional;

public class SystemPropertiesSource implements ConfigSource {

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(System.getProperty(key));
    }

    @Override
    public String name() {
        return "system-properties";
    }
}
