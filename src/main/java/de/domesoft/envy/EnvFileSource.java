package de.domesoft.envy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class EnvFileSource implements ConfigSource {

    private final Properties properties = new Properties();
    private final String path;

    public EnvFileSource(String path) {

        this.path = path;
        loadProperties(this.path);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(properties.getProperty(deNormalizeKey(key)));
    }

    private void loadProperties(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load env file: " + path, e);
        }
    }

    @Override
    public String name() {
        return "EnvFile";
    }

    private String deNormalizeKey(String key) {
        return key == null || key.isEmpty() ? key : key.replace(".", "_").toUpperCase();
    }
}
