# Orisun Java Client

A Java client for the Orisun event store, providing a simple and intuitive interface for interacting with the Orisun gRPC service.

## Features

- **gRPC-based**: Built on gRPC for high-performance communication
- **Type-safe**: Generated from Protocol Buffer definitions
- **Easy to use**: Simple builder pattern for client configuration
- **Authentication**: Support for basic authentication with token caching
- **Dual Clients**: Includes both OrisunClient (event operations) and AdminClient (user management)
- **Load Balancing**: Support for single-server, multi-server, DNS-based, and static load balancing

## Installation

### Download from GitHub Releases

The latest release JAR (with all dependencies included) can be downloaded from the [Releases](https://github.com/oexza/orisun-client-java/releases) page.

Download the `orisun-java-client-{version}.jar` file and follow the instructions below for your build system.

### Using in Your Project

#### Maven

1. Download the JAR file from the [Releases](https://github.com/oexza/orisun-client-java/releases) page
2. Place the JAR in your project's `lib/` directory (or any directory you prefer)
3. Add the following to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.orisunlabs</groupId>
        <artifactId>orisun-java-client</artifactId>
        <version>0.0.1</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib/orisun-java-client-0.0.1.jar</systemPath>
    </dependency>
</dependencies>
```

**Note**: Replace `0.0.1` with the actual version you downloaded and adjust the path if you placed the JAR in a different location.

Alternatively, you can install the JAR to your local Maven repository:

```bash
mvn install:install-file \
  -Dfile=orisun-java-client-0.0.1.jar \
  -DgroupId=com.orisunlabs \
  -DartifactId=orisun-java-client \
  -Dversion=0.0.1 \
  -Dpackaging=jar
```

Then add it as a regular dependency:

```xml
<dependency>
    <groupId>com.orisunlabs</groupId>
    <artifactId>orisun-java-client</artifactId>
    <version>0.0.1</version>
</dependency>
```

#### Gradle

**Option 1: Direct file dependency**

1. Download the JAR file from the [Releases](https://github.com/oexza/orisun-client-java/releases) page
2. Place the JAR in your project's `libs/` directory
3. Add the following to your `build.gradle`:

```groovy
dependencies {
    implementation files('libs/orisun-java-client-0.0.1.jar')
}
```

**Option 2: Flat directory repository**

1. Create a `libs/` directory in your project
2. Place the JAR file in the `libs/` directory
3. Add the following to your `build.gradle`:

```groovy
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    implementation 'com.orisunlabs:orisun-java-client:0.0.1'
}
```

**Option 3: Local Maven repository**

First, install the JAR to your local Maven repository:

```bash
./gradlew publishToMavenLocal
```

Then add it to your project's `build.gradle`:

```groovy
repositories {
    mavenLocal()
}

dependencies {
    implementation 'com.orisunlabs:orisun-java-client:0.0.1'
}
```

### Building from Source

```bash
# Clone the repository
git clone https://github.com/oexza/orisun-client-java.git
cd orisun-client-java

# Initialize submodules
git submodule update --init --recursive

# Build with Gradle
./gradlew build

# The fat JAR with dependencies will be at: build/libs/orisun-client-{version}.jar
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

### OrisunClient - Event Store Operations

```java
import com.orisunlabs.orisun.client.OrisunClient;
import com.orisun.eventstore.Eventstore;
import java.util.UUID;

// Create a client
try (OrisunClient client = OrisunClient.newBuilder()
    .withServer("localhost", 5005)
    .withBasicAuth("admin", "changeit")
    .build()) {

    // Save events
    Eventstore.SaveEventsRequest request = Eventstore.SaveEventsRequest.newBuilder()
        .setBoundary("users")
        .addEvents(Eventstore.EventToSave.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setEventType("UserCreated")
            .setData("{\"userId\":\"user-123\",\"email\":\"john@example.com\"}")
            .build())
        .build();

    Eventstore.WriteResult result = client.saveEvents(request);

    // Read events
    Eventstore.GetEventsResponse response = client.getEvents(
        Eventstore.GetEventsRequest.newBuilder()
            .setBoundary("users")
            .setCount(10)
            .build()
    );
}
```

### AdminClient - User Management

```java
import com.orisunlabs.orisun.client.AdminClient;
import com.orisun.admin.AdminOuterClass.*;

// Create an admin client
try (AdminClient adminClient = AdminClient.newBuilder()
    .withServer("localhost", 5005)
    .withBasicAuth("admin", "changeit")
    .build()) {

    // Create a user
    CreateUserRequest createRequest = CreateUserRequest.newBuilder()
        .setName("John Doe")
        .setUsername("johndoe")
        .setPassword("securePassword123")
        .addRoles("user")
        .build();

    AdminUser user = adminClient.createUser(createRequest);

    // List all users
    List<AdminUser> users = adminClient.listUsers();

    // Validate credentials
    ValidateCredentialsResponse response = adminClient.validateCredentials(
        ValidateCredentialsRequest.newBuilder()
            .setUsername("johndoe")
            .setPassword("securePassword123")
            .build()
    );
}
```

### Advanced Configuration

Both clients support advanced configuration:

```java
OrisunClient client = OrisunClient.newBuilder()
    .withServer("localhost", 5005)
    .withBasicAuth("admin", "changeit")
    .withTimeout(30)                              // Request timeout in seconds
    .withTls(false)                               // Use TLS
    .withLoadBalancingPolicy("round_robin")      // Load balancing policy
    .withKeepAliveTime(30000)                     // Keep-alive time in ms
    .withKeepAliveTimeout(10000)                  // Keep-alive timeout in ms
    .withLogging(true)                            // Enable logging
    .withLogLevel(com.orisunlabs.orisun.client.DefaultLogger.LogLevel.INFO)
    .build();
```

## License

MIT License - see [LICENSE](LICENSE) for details.

## Repository

https://github.com/oexza/orisun-client-java
