package de.domesoft.envy;

import java.util.Optional;

public interface ConfigSource {
    Optional<String> get(String key);
    String name();
}