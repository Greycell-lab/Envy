package de.domesoft.envy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class PropertiesFileSource implements ConfigSource {

    private final Properties properties = new Properties();
    private final String path;

    public PropertiesFileSource(String path) {
        this.path = path;
        loadProperties(path);
    }

    private void loadProperties(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load properties file: " + path, e);
        }
    }
    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    @Override
    public String name() {
        return "properties-file(" + path + ")";
    }
}
