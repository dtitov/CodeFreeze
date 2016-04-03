# CodeFreeze
Deep immutability Java library

[![Build Status](https://drone.io/github.com/dtitov/CodeFreeze/status.png)](https://drone.io/github.com/dtitov/CodeFreeze/latest)

## Features
CodeFreeze library allows you to create immutable versions of POJO beans. The trick is that such immutability is deep:
you will be not allowed not only to change first-level properties of the bean, but any properties on any level of nesting.

Example:

```java
CodeFreeze codeFreeze = new CGLIBCodeFreeze();
User user = userRepository.getUserById(1); // obtaining some User bean
User immutableUser = codeFreeze.freeze(user); // creating deep immutable version of User instance
immutableUser.setName("John Smith"); // this is gonna throw an UnsupportedOperationException
immutableUser.getAddress().setStreet("Broadway"); // this ALSO is gonna throw an UnsupportedOperationException
```

Such behavior can be really useful for security and code-protection purposes in various kinds of cases (e.g. caching).

## Usage
Add the repo to your pom.xml:
```xml
<repositories>
    <repository>
        <id>bracer-mvn-repo</id>
        <url>https://raw.github.com/dtitov/CodeFreeze/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

Then add the dependency:
```xml
        <dependency>
            <groupId>com.autsia</groupId>
            <artifactId>codefreeze</artifactId>
            <version>^</version>
        </dependency>
```
---
*Except as otherwise noted, this library is licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)*
