# ODRE Framework
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser/badge.png?version=0.1.2&style=flat&gav=true)](https://central.sonatype.com/artifact/io.github.odre-framework/odre-core) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Quick install

#### Install
Install latest version from  [maven central repository](https://central.sonatype.com/artifact/io.github.odre-framework/odre-core)

```xml
<dependency>
    <groupId>io.github.odre-framework</groupId>
    <artifactId>odre-core</artifactId>
    <version>0.1.2</version>
</dependency>
```

#### Enforce ODRL policies

````java
String policy = // ODRL policy goes here

Map<String, Object> interpolations = new HashMap<>();
ODRE odre = new ODRE();

Map<String, String> usage = odre.enforce(policy, interpolations);
````
