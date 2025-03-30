package com.yada.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yada.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileService {
    private List<UserProfile> users;
    private static final String DATABASE_FILE = "database.json";
    private final ObjectMapper mapper;

    public UserProfileService() {
        this.users = new ArrayList<>();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDate
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    

    public UserProfile getUserProfile(int userId) {
        return users.stream().filter(user -> user.getId() == userId).findFirst().orElse(null);
    }

    public void addUserProfile(UserProfile userProfile) {
        users.add(userProfile);
    }

    public List<UserProfile> getAllUsers() {
        return users;
    }

    public void saveUserProfiles() {
        try {
            Database database = loadDatabaseFile();
            database.setUsers(users);
            mapper.writeValue(new File(DATABASE_FILE), database);
        } catch (IOException e) {
            System.err.println("Error saving user profiles: " + e.getMessage());
        }
    }

    public void loadUserProfiles() {
        try {
            Database database = loadDatabaseFile();
            this.users = database.getUsers();
        } catch (IOException e) {
            System.err.println("Error loading user profiles: " + e.getMessage());
            this.users = new ArrayList<>();
        }
    }

    private Database loadDatabaseFile() throws IOException {
        File file = new File(DATABASE_FILE);
        if (!file.exists() || file.length() == 0) { // Check if file exists and is non-empty
            System.out.println("User profile database file is empty or missing. Initializing with default values.");
            return new Database(); // Return an empty database object
        }
        return mapper.readValue(file, Database.class);
    }
}