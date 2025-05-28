import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserManager {

    private static final String USERS_FILE = "users.txt";

    public static class User {
        private final String username;
        private final String password; // decrypted

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    // Encrypts a string using Base64 (for demonstration purposes)
    private static String encrypt(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // Decrypts a string using Base64 (for demonstration purposes)
    private static String decrypt(String input) {
        return new String(Base64.getDecoder().decode(input));
    }

    // Loads all users from the file, decrypting their passwords
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = decrypt(parts[1]);
                    users.add(new User(username, password));
                }
            }
        } catch (IOException e) {
            // File may not exist yet, that's fine
        }
        return users;
    }

    // Authenticates a user by username and password
    public boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            String encryptedPassword = encrypt(password);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    if (parts[0].equals(username) && parts[1].equals(encryptedPassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // File may not exist yet, that's fine
        }
        return false;
    }

    // Adds a new user with encrypted password
    public boolean addUser(String username, String password) {
        if (username == null || password == null || username.contains(":") || password.contains(":")) {
            return false;
        }
        // Check if user already exists
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(username + ":" + encrypt(password));
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}