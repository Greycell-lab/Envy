package de.domesoft.envy;

import java.util.List;
import java.util.Optional;

public class PrefixEnvy extends Envy {

    private final Envy parent;
    private final String prefix;

    public PrefixEnvy(Envy parent, String prefix) {
        super(List.of());
        this.parent = parent;
        this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
    }

    @Override
    public Optional<String> get(String key) {
        return parent.get(prefix + key);
    }

    public Envy prefix(String prefix) {
        return new PrefixEnvy(this, prefix);
    }
}
