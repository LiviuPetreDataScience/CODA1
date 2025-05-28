import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String USERS_FILE = "users.txt";
    private static final String HASH_ALGORITHM = "SHA-256";
    private Map<String, String> users;

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    private void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // File may not exist on first run; that's fine.
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users", e);
        }
    }

    public boolean registerUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (users.containsKey(username)) {
            return false;
        }
        String hashedPassword = hashPassword(password);
        users.put(username, hashedPassword);
        saveUsers();
        return true;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        String storedHash = users.get(username);
        if (storedHash == null) {
            return false;
        }
        String inputHash = hashPassword(password);
        return storedHash.equals(inputHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found", e);
        }
    }
}