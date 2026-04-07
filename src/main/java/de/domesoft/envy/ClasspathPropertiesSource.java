package de.domesoft.envy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class ClasspathPropertiesSource implements ConfigSource {

    private final Properties properties = new Properties();
    private final String resourceName;

    public ClasspathPropertiesSource(String resourceName) {
        this.resourceName = resourceName;
        load(resourceName);
    }

    private void load(String resourceName) {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Classpath resource not found: " + resourceName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load classpath resource: " + resourceName, e);
        }
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    @Override
    public String name() {
        return "ClasspathPropertiesSource[" + resourceName + "]";
    }
}
