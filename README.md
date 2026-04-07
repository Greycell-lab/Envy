# Envy

Tiny Java configuration library with explicit source priority.

Envy is a small Java library for reading configuration values from multiple sources such as system properties, environment variables, and `.properties` files. Values are resolved in the exact order the sources are added to the builder, so the first matching source wins.

## Features

- Explicit source priority
- JVM system properties support
- Environment variable support
- `.properties` file support
- Required vs optional configuration access
- Typed getters for `int`, `long`, `double`, and `boolean`
- Default values for missing keys

## Why Envy exists

Java applications often need configuration from multiple places: local files, environment variables, and JVM system properties. Envy keeps that problem intentionally small and framework-neutral.

The goal is simple:

- predictable lookup order
- minimal API
- fail-fast for required values
- convenient defaults for optional values

## Installation

If published as a Maven artifact, the dependency would look like this:

```xml
<dependency>
    <groupId>de.domesoft</groupId>
    <artifactId>envy</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Quick Start

```java
Envy envy = Envy.builder()
        .source(new SystemPropertiesSource())
        .source(new EnvSource())
        .source(new PropertiesFileSource("app.properties"))
        .build();
```

## Source Priority

Sources are checked in the order they are added.

```java
Envy envy = Envy.builder()
        .source(new SystemPropertiesSource())
        .source(new EnvSource())
        .source(new PropertiesFileSource("app.properties"))
        .build();
```

In this example the lookup order is:

1. System properties
2. Environment variables
3. `app.properties`

If the same key exists in multiple sources, the value from the first matching source is returned.

## API Overview

### Optional value lookup

Use `get(key)` when a value may or may not exist.

```java
Optional<String> appName = envy.get("app.name");
```

### Value lookup with default

Use `get(key, defaultValue)` when you want a guaranteed return value.

```java
String appName = envy.get("app.name", "Default App");
String missing = envy.get("unknown.key", "fallback");
```

The default value may also be `null`.

```java
String optionalKey = envy.get("optional.key", null);
```

### Required values

Use `require(key)` for values that must exist.

```java
String dbHost = envy.require("db.host");
```

If the key is missing in all sources, Envy throws `MissingConfigException`.

## Typed Getters

### Integer

```java
Optional<Integer> timeout = envy.getInt("app.timeout");
int timeoutValue = envy.getInt("app.timeout", 30);
```

### Long

```java
Optional<Long> maxSize = envy.getLong("app.maxSize");
long maxSizeValue = envy.getLong("app.maxSize", 1000L);
```

### Double

```java
Optional<Double> ratio = envy.getDouble("app.ratio");
double ratioValue = envy.getDouble("app.ratio", 0.75);
```

### Boolean

```java
Optional<Boolean> debug = envy.getBoolean("app.debug");
boolean debugEnabled = envy.getBoolean("app.debug", false);
```

## Boolean conversion behavior

Boolean conversion uses Java boolean parsing.

- `"true"` (ignoring case) becomes `true`
- every other value becomes `false`

Examples:

```java
envy.getBoolean("feature.enabled");   // "true" -> true
envy.getBoolean("feature.enabled");   // "TRUE" -> true
envy.getBoolean("feature.enabled");   // "yes" -> false
envy.getBoolean("feature.enabled");   // "abc" -> false
```

## Numeric conversion behavior

If a key exists but cannot be converted to the requested numeric type, Envy throws `ConfigConversionException`.

Example:

```java
int timeout = envy.getInt("app.timeout").orElseThrow();
```

If `app.timeout=abc`, a `ConfigConversionException` is thrown.

## Example `app.properties`

If you use `PropertiesFileSource("app.properties")`, the file should be available via the given file path.

Example:

```properties
app.name=Envy Demo
app.timeout=30
app.debug=true
db.host=localhost
db.port=5432
```

## Example Usage

```java
Envy envy = Envy.builder()
        .source(new SystemPropertiesSource())
        .source(new EnvSource())
        .source(new PropertiesFileSource("app.properties"))
        .build();

String appName = envy.get("app.name", "My App");
int timeout = envy.getInt("app.timeout", 30);
boolean debug = envy.getBoolean("app.debug", false);
String dbHost = envy.require("db.host");
```

## Error Handling

Envy currently uses two main exceptions:

- `MissingConfigException` for missing required keys
- `ConfigConversionException` for invalid type conversion

Example:

```java
String host = envy.require("db.host"); // throws if missing
int timeout = envy.getInt("app.timeout").orElseThrow(); // throws if invalid
```

## Testing

Typical test cases for Envy include:

- source priority works as expected
- required values throw when missing
- default values are returned when keys are missing
- numeric conversion throws on invalid input

## Roadmap

Possible next improvements:

- `has(key)`
- classpath resource support
- prefix-based configuration groups
- generic converter support
- stricter boolean parsing

## License

MIT License (or your preferred license)