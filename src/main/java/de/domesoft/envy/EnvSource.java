package de.domesoft.envy;

import java.util.Optional;

public class EnvSource implements ConfigSource {

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(System.getenv(key));
    }

    @Override
    public String name() {
        return "environment";
    }
}
