# Envy

Envy is a lightweight Java configuration library for loading application settings from multiple sources.

It supports:
- classpath properties files
- properties files from the filesystem
- `.env` files
- environment variables
- JVM system properties

Envy provides three access styles:
- optional values via `Optional`
- direct values with defaults
- required values that throw an exception when missing

## Features

- Multiple configuration sources
- Prefix-based configuration via builder
- Optional access with `Optional<T>`
- Typed access for:
    - `String`
    - `int`
    - `long`
    - `double`
    - `boolean`
- Default values
- Required values via `require(...)`
- Simple builder API

## Installation

Add the dependency to your Maven project:

```xml
<dependency>
    <groupId>de.domesoft</groupId>
    <artifactId>envy</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### Example properties file

```properties
db.host=localhost
db.port=5432
app.name=EnvyDemo
feature.enabled=true
```

### Basic usage

```java
Envy envy = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .build();

String host = envy.get("db.host", "127.0.0.1");
int port = envy.getInt("db.port", 3306);
boolean enabled = envy.getBoolean("feature.enabled", false);
```

## Builder

Create a new `Envy` instance with the builder:

```java
Envy envy = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .build();
```

You can register multiple sources:

```java
Envy envy = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .source(new EnvFileSource(".env"))
        .source(new SystemPropertiesSource())
        .build();
```

## Source priority

Sources are checked in the order in which they are added to the builder.

The first source that contains a value for the requested key wins.

```java
Envy envy = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .source(new EnvFileSource(".env"))
        .build();
```

In this example, `ClasspathPropertiesSource` is checked first.  
If it already contains `db.host`, that value is returned and `EnvFileSource` is not checked afterwards. [file:1088][file:1089]

## Prefix support

You can build a prefixed configuration view with the builder:

```java
Envy dbConfig = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .prefix("db")
        .build();
```

Then access keys relative to that prefix:

```java
String host = dbConfig.get("host", "localhost");
int port = dbConfig.getInt("port", 5432);
```

With a prefix of `db`, Envy will look up:
- `host` as `db.host`
- `port` as `db.port` [file:1088][file:1091]

## Access patterns

### Optional access

Use these methods when a value may be missing:

```java
Optional<String> host = envy.get("db.host");
Optional<Integer> port = envy.getInt("db.port");
Optional<Long> timeout = envy.getLong("app.timeout");
Optional<Double> factor = envy.getDouble("calc.factor");
Optional<Boolean> enabled = envy.getBoolean("feature.enabled");
```

### Access with default values

Use these methods when you want a fallback value:

```java
String host = envy.get("db.host", "localhost");
int port = envy.getInt("db.port", 5432);
long timeout = envy.getLong("app.timeout", 5000L);
double factor = envy.getDouble("calc.factor", 1.0);
boolean enabled = envy.getBoolean("feature.enabled", false);
```

### Required values

Use `require(...)` when a value must be present:

```java
String host = envy.require("db.host");
```

If the key is missing, Envy throws `MissingConfigException`. [file:1089][file:1090]

## Supported types

Envy currently supports the following typed access methods:

- `get(String key)`
- `get(String key, String defaultValue)`
- `require(String key)`

- `getInt(String key)`
- `getInt(String key, int defaultValue)`

- `getLong(String key)`
- `getLong(String key, long defaultValue)`

- `getDouble(String key)`
- `getDouble(String key, double defaultValue)`

- `getBoolean(String key)`
- `getBoolean(String key, boolean defaultValue)`

- `has(String key)` [file:1089]

## Available sources

### `ClasspathPropertiesSource`

Loads a `.properties` file from the classpath.

```java
Envy envy = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .build();
```

Example file location:
- `src/main/resources/app.properties` [file:1083]

### `PropertiesFileSource`

Loads a `.properties` file from the filesystem.

```java
Envy envy = Envy.builder()
        .source(new PropertiesFileSource("/etc/myapp/app.properties"))
        .build();
```

[file:1092]

### `EnvFileSource`

Loads a `.env`-style file from the filesystem.

Example `.env` file:

```env
DB_HOST=localhost
DB_PORT=5432
FEATURE_ENABLED=true
```

Usage:

```java
Envy envy = Envy.builder()
        .source(new EnvFileSource(".env"))
        .build();
```

When you request:

```java
envy.get("db.host")
```

`EnvFileSource` internally maps that to:

```text
DB_HOST
```

[file:1084]

### `EnvSource`

Reads values from system environment variables using `System.getenv(...)`.

```java
Envy envy = Envy.builder()
        .source(new EnvSource())
        .build();
```

[file:1087]

### `SystemPropertiesSource`

Reads values from JVM system properties using `System.getProperty(...)`.

Example:

```bash
java -Ddb.host=localhost -jar app.jar
```

Usage:

```java
Envy envy = Envy.builder()
        .source(new SystemPropertiesSource())
        .build();
```

[file:1093]

## Example: combine multiple sources

```java
Envy envy = Envy.builder()
        .source(new SystemPropertiesSource())
        .source(new EnvSource())
        .source(new EnvFileSource(".env"))
        .source(new ClasspathPropertiesSource("app.properties"))
        .build();
```

This setup can be useful if you want to check:
1. JVM system properties first
2. then environment variables
3. then a local `.env` file
4. then default values from `app.properties`

Because Envy checks sources in registration order, the first matching value is returned. [file:1088][file:1089]

## Boolean conversion

Boolean values are parsed case-insensitively.

Valid values:
- `true`
- `false`

Example:

```properties
feature.enabled=true
```

```java
boolean enabled = envy.getBoolean("feature.enabled", false);
```

If a value exists but cannot be converted, Envy throws `ConfigConversionException`. [file:1089][file:1085]

## Numeric conversion

The following conversions are supported:
- `int`
- `long`
- `double`

If the configured value is not a valid number, Envy throws `ConfigConversionException`. [file:1089][file:1085]

## Exceptions

### `MissingConfigException`

Thrown by:

```java
envy.require("some.key");
```

when the requested key is missing. [file:1089][file:1090]

### `ConfigConversionException`

Thrown when a value exists but cannot be converted to the requested type.

Examples:
- `db.port=abc` with `getInt("db.port")`
- `feature.enabled=maybe` with `getBoolean("feature.enabled")` [file:1089][file:1085]

## Example project setup

### `app.properties`

```properties
db.host=localhost
db.port=5432
db.user=app
feature.enabled=true
```

### Java code

```java
Envy envy = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .build();

Envy db = Envy.builder()
        .source(new ClasspathPropertiesSource("app.properties"))
        .prefix("db")
        .build();

String host = db.get("host", "localhost");
int port = db.getInt("port", 5432);
boolean enabled = envy.getBoolean("feature.enabled", false);
```

## Current behavior notes

- Sources are checked in registration order.
- The first matching source wins.
- Prefix support is available through the builder via `.prefix(...)`.
- Prefix access is implemented internally through `PrefixEnvy`.
- `require(...)` currently exists for `String` keys.
- Typed required methods like `requireInt(...)`, `requireLong(...)` or `requireBoolean(...)` are not currently part of the API. [file:1088][file:1089][file:1091]
