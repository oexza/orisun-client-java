# Orisun Java Client

A Java client for the Orisun event store, providing a simple and intuitive interface for interacting with the Orisun gRPC service.

## Features

- **gRPC-based**: Built on gRPC for high-performance communication
- **Type-safe**: Generated from Protocol Buffer definitions
- **Easy to use**: Simple builder pattern for client configuration
- **Authentication**: Support for basic authentication

## Installation

### Maven

```xml
<dependency>
    <groupId>com.orisunlabs</groupId>
    <artifactId>orisun-java-client</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.orisunlabs:orisun-java-client:0.0.1'
```

## Building from Source

```bash
# Clone the repository
git clone https://github.com/oexza/orisun-client-java.git
cd orisun-client-java

# Initialize submodules
git submodule update --init --recursive

# Build with Gradle
./gradlew build
```

## Proto Files

This client uses proto definitions from a submodule. To update the proto files:

```bash
# Update the submodule
git submodule update --remote protos

# Rebuild the project
./gradlew build
```

## Quick Start

```java
import com.orisunlabs.orisun.client.OrisunClient;

// Create a client
OrisunClient client = OrisunClient.newBuilder()
    .withServer("localhost", 5005)
    .withBasicAuth("admin", "changeit")
    .build();

// Use the client...
```

## License

MIT License - see [LICENSE](LICENSE) for details.

## Repository

https://github.com/oexza/orisun-client-java
