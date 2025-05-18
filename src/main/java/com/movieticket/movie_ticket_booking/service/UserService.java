package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.User;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final String userFile = "data/users.txt";

    // Admin credentials (static)
    private static final String ADMIN_EMAIL = "admin@movievault.com";
    private static final String ADMIN_PASSWORD = "admin123";

    public UserService() {
        initializeDataDirectory();
    }

    private void initializeDataDirectory() {
        try {
            Files.createDirectories(Paths.get("data"));
            if (!Files.exists(Paths.get(userFile))) {
                Files.createFile(Paths.get(userFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Admin Authentication ---
    public User authenticateAdmin(String email, String password) {
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            return createAdminUser();
        }
        return null;
    }

    private User createAdminUser() {
        User admin = new User("Admin", "User", ADMIN_EMAIL, "Male", "ADMIN", "Movie Vault HQ", "1234567890", "");
        admin.setId("admin");
        admin.setRole("ADMIN");
        return admin;
    }

    // --- User Management ---
    public User registerUser(User user) throws IOException {
        if (findUserByEmail(user.getEmail()) != null) return null;
        user.setId(UUID.randomUUID().toString());
        user.setRole("USER");
        saveUser(user);
        return user;
    }

    public void saveUser(User user) throws IOException {
        System.out.println("[DEBUG] Saving user to: " + Paths.get(userFile).toAbsolutePath());
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(userFile), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String userLine = formatUserForStorage(user);
            System.out.println("[DEBUG] User data: " + userLine);
            writer.write(userLine);
            writer.newLine();
        }
    }

    private String formatUserForStorage(User user) {
        return String.join(",",
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getNic(),
                user.getAddress(),
                user.getMobile(),
                user.getPassword(),
                user.getRole());
    }


    public User findUserByEmailAndPassword(String email, String password) throws IOException {
        System.out.println("[DEBUG] Login attempt: " + email + " / " + password);

        User admin = authenticateAdmin(email, password);
        if (admin != null) {
            System.out.println("[DEBUG] Admin login successful for: " + email);
            return admin;
        }

        for (User u : readUsersFromFile()) {
            System.out.println("[DEBUG] Checking: " + u.getEmail() + " / " + u.getPassword());
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                System.out.println("[DEBUG] User match found: " + u.getEmail());
                return u;
            }
        }
        System.out.println("[DEBUG] No user match found for: " + email);
        return null;
    }

    public User findUserByEmail(String email) throws IOException {
        if (ADMIN_EMAIL.equals(email)) return createAdminUser();
        return readUsersFromFile().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }


    public List<User> getAllUsers() throws IOException {
        return readUsersFromFile();
    }

    public User getUserById(String userId) throws IOException {
        return readUsersFromFile().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    private List<User> readUsersFromFile() throws IOException {
        if (!Files.exists(Paths.get(userFile))) return new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(userFile))) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 10)
                    .map(parts -> {
                        User user = new User(
                                parts[1], parts[2], parts[3], parts[4],
                                parts[5], parts[6], parts[7], parts[8]
                        );
                        user.setId(parts[0]);
                        user.setRole(parts[9]);
                        return user;
                    })
                    .collect(Collectors.toList());
        }
    }
}
