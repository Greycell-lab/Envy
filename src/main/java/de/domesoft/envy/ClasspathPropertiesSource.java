package de.domesoft.envy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * Configuration source that loads properties from a file on the classpath.
 * The properties file is loaded once during construction and cached for subsequent lookups.
 *
 * <p>Example usage:</p>
 * <pre>
 * var source = new ClasspathPropertiesSource("app.properties");
 * </pre>
 */
public class ClasspathPropertiesSource implements ConfigSource {

    private final Properties properties = new Properties();
    private final String resourceName;

    /**
     * Creates a new ClasspathPropertiesSource that loads the specified resource from the classpath.
     *
     * @param resourceName the name of the resource file on the classpath (e.g., "app.properties")
     * @throws IllegalArgumentException if the resource cannot be found or loaded
     */
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
