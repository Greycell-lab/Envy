package de.domesoft.envy;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EnvyBuilderTest {
    @Test
    void testSystemPropertiesOverrideEnv() {
        System.setProperty("test.key", "system-value");

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
}
