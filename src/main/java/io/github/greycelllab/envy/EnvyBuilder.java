package io.github.greycelllab.envy;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for creating {@link Envy} instances with custom configuration sources and prefixes.
 *
 * <p>Example usage:</p>
 * <pre>
 * var envy = Envy.builder()
 *         .source(new ClasspathPropertiesSource("app.properties"))
 *         .source(new EnvSource())
 *         .prefix("db")
 *         .build();
 * </pre>
 */
public class EnvyBuilder {

    private final List<ConfigSource> sources = new ArrayList<>();

    private String prefix;

    /**
     * Adds a configuration source to the builder. Sources are checked in the order they are added.
     *
     * @param source the configuration source to add
     * @return this builder instance for method chaining
     */
    public EnvyBuilder source(ConfigSource source) {
        this.sources.add(source);
        return this;
    }

    /**
     * Sets a prefix for all configuration keys. If set, all lookups will be prefixed with this value.
     * For example, with prefix "db", a lookup for "host" will search for "db.host".
     *
     * @param prefix the prefix to use for all configuration keys
     * @return this builder instance for method chaining
     */
    public EnvyBuilder prefix(String prefix) {
        this.prefix = prefix.strip();
        return this;
    }

    /**
     * Builds and returns a {@link ConfigurableEnvy} instance with the configured sources and prefix.
     *
     * @return a new ConfigurableEnvy instance
     */
    public ConfigurableEnvy build() {
        Envy baseEnvy = new Envy(sources);
        return prefix == null || prefix.isBlank() ? baseEnvy : new PrefixEnvy(baseEnvy, prefix);
    }
}
