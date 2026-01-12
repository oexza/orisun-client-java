package com.orisunlabs.orisun.examples;

import com.orisunlabs.orisun.client.AdminClient;
import com.orisunlabs.orisun.client.DefaultLogger;
import com.orisunlabs.orisun.client.OrisunException;
import com.orisun.admin.AdminOuterClass.*;

import java.util.List;

/**
 * Usage example for Orisun Admin Client
 */
public class AdminUsageExample {

    public static void main(String[] args) {
        // Create admin client with comprehensive configuration
        try (final var adminClient = AdminClient.newBuilder()
                .withServer("localhost", 5005)
                .withBasicAuth("admin", "changeit")
                .withLogging(true)
                .withLogLevel(DefaultLogger.LogLevel.INFO)
                .withTimeout(30)
                .build()) {

            System.out.println("🔌 Connecting to Orisun Admin Service...");

            // Test connection by getting user count
            long userCount = adminClient.getUserCount();
            System.out.println("✅ Connected successfully!");
            System.out.println("📊 Current user count: " + userCount);

            // Example 1: Create a new user
            createUserExample(adminClient);

            // Example 2: List all users
            listUsersExample(adminClient);

            // Example 3: Validate credentials
            validateCredentialsExample(adminClient);

            // Example 4: Get event count for a boundary
            getEventCountExample(adminClient);

            // Example 5: Change password
            changePasswordExample(adminClient);

            // Example 6: Delete user
            deleteUserExample(adminClient);

            System.out.println("✅ Admin examples completed!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createUserExample(AdminClient adminClient) {
        System.out.println("\n👤 Creating a new user...");

        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setName("Jane Smith")
                .setUsername("janesmith")
                .setPassword("securePassword123")
                .addRoles("user")
                .addRoles("editor")
                .build();

        try {
            AdminUser user = adminClient.createUser(request);
            System.out.println("✅ User created successfully!");
            System.out.println("   ID: " + user.getUserId());
            System.out.println("   Name: " + user.getName());
            System.out.println("   Username: " + user.getUsername());
            System.out.println("   Roles: " + String.join(", ", user.getRolesList()));
        } catch (OrisunException e) {
            System.err.println("❌ Failed to create user: " + e.getMessage());
        }
    }

    private static void listUsersExample(AdminClient adminClient) {
        System.out.println("\n📋 Listing all users...");

        try {
            List<AdminUser> users = adminClient.listUsers();
            System.out.println("✅ Found " + users.size() + " users:");

            for (AdminUser user : users) {
                System.out.println("   - " + user.getUsername() + " (" + user.getName() + ")");
                System.out.println("     ID: " + user.getUserId());
                System.out.println("     Roles: " + String.join(", ", user.getRolesList()));
            }
        } catch (OrisunException e) {
            System.err.println("❌ Failed to list users: " + e.getMessage());
        }
    }

    private static void validateCredentialsExample(AdminClient adminClient) {
        System.out.println("\n🔐 Validating user credentials...");

        ValidateCredentialsRequest request = ValidateCredentialsRequest.newBuilder()
                .setUsername("janesmith")
                .setPassword("securePassword123")
                .build();

        try {
            ValidateCredentialsResponse response = adminClient.validateCredentials(request);
            if (response.getSuccess()) {
                System.out.println("✅ Credentials validated successfully!");
                System.out.println("   User: " + response.getUser().getName());
            } else {
                System.out.println("❌ Invalid credentials");
            }
        } catch (OrisunException e) {
            System.err.println("❌ Failed to validate credentials: " + e.getMessage());
        }
    }

    private static void getEventCountExample(AdminClient adminClient) {
        System.out.println("\n📊 Getting event count...");

        String boundary = "users";
        try {
            long eventCount = adminClient.getEventCount(boundary);
            System.out.println("✅ Event count for boundary '" + boundary + "': " + eventCount);
        } catch (OrisunException e) {
            System.err.println("❌ Failed to get event count: " + e.getMessage());
        }
    }

    private static void changePasswordExample(AdminClient adminClient) {
        System.out.println("\n🔑 Changing user password...");

        // Note: In a real scenario, you would have a valid user ID
        String userId = "user-123"; // This would come from a previous operation
        ChangePasswordRequest request = ChangePasswordRequest.newBuilder()
                .setUserId(userId)
                .setCurrentPassword("securePassword123")
                .setNewPassword("newSecurePassword456")
                .build();

        try {
            boolean success = adminClient.changePassword(request);
            if (success) {
                System.out.println("✅ Password changed successfully!");
            } else {
                System.out.println("❌ Failed to change password");
            }
        } catch (OrisunException e) {
            System.err.println("❌ Failed to change password: " + e.getMessage());
        }
    }

    private static void deleteUserExample(AdminClient adminClient) {
        System.out.println("\n🗑️  Deleting a user...");

        // Note: In a real scenario, you would have a valid user ID
        String userId = "user-123"; // This would come from a previous operation
        DeleteUserRequest request = DeleteUserRequest.newBuilder()
                .setUserId(userId)
                .build();

        try {
            boolean success = adminClient.deleteUser(request);
            if (success) {
                System.out.println("✅ User deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete user");
            }
        } catch (OrisunException e) {
            System.err.println("❌ Failed to delete user: " + e.getMessage());
        }
    }
}