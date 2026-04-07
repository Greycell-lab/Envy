package de.domesoft.envy;

import java.util.ArrayList;
import java.util.List;

public class EnvyBuilder {

    private final List<ConfigSource> sources = new ArrayList<>();

    private String prefix;

    public EnvyBuilder source(ConfigSource source) {
        this.sources.add(source);
        return this;
    }

    public EnvyBuilder prefix(String prefix) {
        this.prefix = prefix.strip();
        return this;
    }

    public Envy build() {
        Envy baseEnvy = new Envy(sources);
        return prefix == null || prefix.isBlank() ? baseEnvy : new PrefixEnvy(baseEnvy, prefix);
    }
}
