package de.domesoft.envy;

import java.util.ArrayList;
import java.util.List;

public class EnvyBuilder {

    private final List<ConfigSource> sources = new ArrayList<>();

    public EnvyBuilder source(ConfigSource source) {
        this.sources.add(source);
        return this;
    }

    public Envy build() {
        return new Envy(sources);
    }
}
