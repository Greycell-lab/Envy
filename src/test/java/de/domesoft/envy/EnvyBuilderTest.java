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
        Envy envy = Envy.builder()
                .source(new SystemPropertiesSource())
                .source(new EnvSource())
                .source(new PropertiesFileSource("app.properties"))
                .build();

        assertEquals("system-value", envy.require("test.key"));
        assertTrue(envy.get("app.name").isPresent());
        assertEquals("Envy Demo", envy.get("app.name").get());
    }

    @Test
    void testRequireThrowsOnMissing() {
        Envy envy = Envy.builder()
                .source(new SystemPropertiesSource())
                .build();

        assertThrows(MissingConfigException.class, () -> envy.require("missing.key"));
    }

    @Test
    void testDefaultValue() {
        Envy envy = Envy.builder()
                .source(new SystemPropertiesSource())
                .source(new EnvSource())
                .source(new PropertiesFileSource("app.properties"))
                .build();

        assertEquals(Optional.of("Envy Demo"), envy.get("app.name"));
        assertEquals("DEFAULT", envy.get("test.test", "DEFAULT"));
        assertEquals(30, envy.getInt("app.timeout", 55));
    }

    @Test
    void testClasspathPropertiesSource() {
        Envy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();

        assertEquals(Optional.of("localhorst"), envy.get("db.host"));
        assertEquals(Optional.of(false), envy.getBoolean("app.debug"));
        assertEquals(5555, envy.getInt("app.poort", 5555));
    }

    @Test
    void testPrefixEnvy() {
        Envy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .build();
        PrefixEnvy prefixEnvy = new PrefixEnvy(envy, "db.");

        assertEquals(Optional.of(54325), prefixEnvy.getInt("port"));
        assertEquals(Optional.of("localhorst"), prefixEnvy.get("host"));
        assertEquals("localhorst", prefixEnvy.require("host"));
    }

    @Test
    void testDirectPrefixEnvyBuilder() {
        Envy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .prefix("db")
                .build();
        assertEquals(Optional.of(54325), envy.getInt("port"));
        assertThrows(MissingConfigException.class, () -> envy.require("log"));
    }

    @Test
    void testInvalidPrefix() {
        Envy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .prefix("dB")
                .build();
        assertEquals(Optional.empty(), envy.getInt("port"));
    }

    @Test
    void testValidPrefixWithWhitespaces() {
        Envy envy = Envy.builder()
                .source(new ClasspathPropertiesSource("app.properties"))
                .prefix("  db  ")
                .build();
        assertEquals(Optional.of(54325), envy.getInt("port"));
    }

}
