# Envy

Tiny Java configuration library with explicit source priority.

Envy is a small Java library for reading configuration values from multiple sources such as system properties, environment variables, and `.properties` files. It resolves values in the exact order the sources are added, so the first matching value wins.

## Features

- Explicit source priority, the order of `.source(...)` calls defines precedence
- Supports JVM system properties via `System.getProperty(...)`
- Supports environment variables via `System.getenv(...)`
- Supports Java `.properties` files using `java.util.Properties`
- Distinguishes between required values and optional values
- Typed getters for `String`, `int`, `long`, `double`, and `boolean`

## Why Envy exists

Java applications often need configuration from different places: local files, OS environment variables, and JVM system properties. Envy keeps that idea intentionally small: no framework required, no hidden magic, just predictable lookup rules and a small API.

## How source priority works

Sources are checked **in the order they are added to the builder**. The first source that contains a value for a given key wins.

```java
Envy envy = Envy.builder()
    .source(new SystemPropertiesSource())
    .source(new EnvSource())
    .source(new PropertiesFileSource("app.properties"))
    .build();
```

In this example, the priority is:
1. **System properties** (highest)
2. **Environment variables**
3. **`app.properties`** (lowest)

If `db.host` exists in all three places, Envy returns the value from **system properties** because that source is checked first.

## Required vs optional values

Envy separates **required** configuration from **optional** configuration.

### `require(key)` - Pflichtwerte

Use `require(...)` for values that **must exist** or the application should fail immediately.

```java
String host = envy.require("db.host");  // Muss da sein!
```

**Throws:** `MissingConfigException` if the key is missing in all sources.

### `getXXX(key)` - Optionale Werte

Use typed getters when the value is **optional** or when a default makes sense.

```java
int port = envy.getInt("db.port").orElse(5432);           // 5432 als Fallback
boolean debug = envy.getBoolean("app.debug").orElse(false); // false als Fallback
```

**Returns:** `Optional.empty()` if key doesn't exist. **Throws:** `ConfigConversionException` if value can't be converted.

## Example `app.properties`

Place `app.properties` **next to `pom.xml`** (for your current `FileInputStream` loader):

```properties
# app.properties
db.host=localhost
db.port=5432
app.debug=true
app.name=Envy Demo
app.timeout=30
```

## Complete Usage Example

```java
public class Main {
    public static void main(String[] args) {
        Envy envy = Envy.builder()
            .source(new SystemPropertiesSource())
            .source(new EnvSource())
            .source(new PropertiesFileSource("app.properties"))
            .build();

        // Pflichtwerte (fail-fast)
        String host = envy.require("db.host");
        
        // Optionale Werte mit Default
        int port = envy.getInt("db.port").orElse(5432);
        boolean debug = envy.getBoolean("app.debug").orElse(false);
        
        System.out.println("DB: " + host + ":" + port);
        System.out.println("Debug: " + debug);
    }
}
```

## When to use `require()` vs `getXXX()`

| Pflicht (require) | Optional (getXXX) |
|-------------------|-------------------|
| `db.host` | `app.debug` |
| `api.key` | `timeout` |
| `kafka.topic` | `max.threads` |
| `http.port` | `log.level` |

## Maven coordinates (when published)

```xml
<groupId>de.domesoft</groupId>
<artifactId>envy</artifactId>
<version>0.1.0</version>
```

## Planned Features

- ✅ **MVP**: System Props, ENV, Properties files
- ⏳ **Next**: Classpath resources (`src/main/resources`)
- ⏳ **Later**: List support, prefix groups, Record binding

## License

MIT License (add when publishing)