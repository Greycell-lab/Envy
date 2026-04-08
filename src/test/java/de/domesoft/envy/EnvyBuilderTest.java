package de.domesoft.envy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EnvyBuilderTest {

    private String oldTestKey;

    @BeforeEach
    void setUp() {
        oldTestKey = System.getProperty("test.key");
        System.setProperty("test.key", "system-value");
    }

    @AfterEach
    void tearDown() {
        if (oldTestKey == null) {
            System.clearProperty("test.key");
        } else {
            System.setProperty("test.key", oldTestKey);
        }
    }

    @Test
    void testSystemPropertiesOverrideEnv() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new SystemPropertiesSource())
                .source(new EnvSource())
                .source(new PropertiesFileSource("src/test/resources/app.properties"))
                .build();

        assertEquals("system-value", envy.require("test.key"));
        assertTrue(envy.get("app.name").isPresent());
        assertEquals("Envy Demo", envy.get("app.name").get());
    }

    @Test
    void testRequireThrowsOnMissing() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new SystemPropertiesSource())
                .build();

        assertThrows(MissingConfigException.class, () -> envy.require("missing.key"));
    }

    @Test
    void testDefaultValue() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new SystemPropertiesSource())
                .source(new EnvSource())
                .source(new PropertiesFileSource("src/test/resources/app.properties"))
                .build();

        assertEquals(Optional.of("Envy Demo"), envy.get("app.name"));
        assertEquals("DEFAULT", envy.get("test.test", "DEFAULT"));
        assertEquals(50, envy.getInt("app.timeout", 55));
    }

    @Test
    void testClasspathPropertiesSource() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();

        assertEquals(Optional.of("localhorst"), envy.get("db.host"));
        assertEquals(Optional.of(false), envy.getBoolean("app.debug"));
        assertEquals(5555, envy.getInt("app.poort", 5555));
    }

    @Test
    void testPrefixEnvy() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();
        PrefixEnvy prefixEnvy = new PrefixEnvy(envy, "db.");

        assertEquals(Optional.of(54325), prefixEnvy.getInt("port"));
        assertEquals(Optional.of("localhorst"), prefixEnvy.get("host"));
        assertEquals("localhorst", prefixEnvy.require("host"));
        assertEquals(54325, prefixEnvy.requireInt("port"));
        assertEquals(54325L, prefixEnvy.requireLong("port"));
    }

    @Test
    void testDirectPrefixEnvyBuilder() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .prefix("db")
                .build();
        assertEquals(Optional.of(54325), envy.getInt("port"));
        assertThrows(MissingConfigException.class, () -> envy.require("log"));
    }

    @Test
    void testInvalidPrefix() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .prefix("dB")
                .build();
        assertEquals(Optional.empty(), envy.getInt("port"));
    }

    @Test
    void testValidPrefixWithWhitespaces() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .prefix("  db  ")
                .build();
        assertEquals(Optional.of(54325), envy.getInt("port"));
    }

    @Test
    void testEnvFileSource() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new EnvFileSource("src/test/resources/properties.env"))
                .build();
        assertEquals(Optional.of("localhost"), envy.get("db.host"));
        assertEquals(Optional.of("5050"), envy.get("db.port"));
    }

    @Test
    void testEnvOverrideProperty() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new EnvFileSource("src/test/resources/properties.env"))
                .source(new PropertiesFileSource("src/test/resources/app.properties"))
                .build();
        assertEquals(Optional.of(5050), envy.getInt("db.port"));
        assertEquals(Optional.of("Envy Demo"), envy.get("app.name"));
    }

    @Test
    void testRequireInt() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();

        assertEquals(54325, envy.requireInt("db.port"));
        assertThrows(MissingConfigException.class, () -> envy.requireInt("missing.key"));
    }

    @Test
    void testRequireLong() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();

        assertEquals(54325L, envy.requireLong("db.port"));
        assertThrows(MissingConfigException.class, () -> envy.requireLong("missing.key"));
    }

    @Test
    void testRequireDouble() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();

        assertEquals(50.0, envy.requireDouble("app.timeout"));
        assertThrows(MissingConfigException.class, () -> envy.requireDouble("missing.key"));
    }

    @Test
    void testRequireBoolean() {
        ConfigurableEnvy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();

        assertEquals(false, envy.requireBoolean("app.debug"));
        assertThrows(MissingConfigException.class, () -> envy.requireBoolean("missing.key"));
    }
}
