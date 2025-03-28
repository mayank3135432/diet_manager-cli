package com.yada.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yada.model.*;
import java.io.*;



public class UserProfileService {
    private UserProfile userProfile;
    private static final String PROFILE_FILE = "profile.json";
    private final ObjectMapper mapper;

    public UserProfileService() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    public void setUserProfile(UserProfile profile) {
        this.userProfile = profile;
        saveUserProfile();
    }

    public UserProfile getUserProfile() {
        if (userProfile == null) {
            loadUserProfile();
        }
        return userProfile;
    }

    private void saveUserProfile() {
        try {
            mapper.writeValue(new File(PROFILE_FILE), userProfile);
        } catch (IOException e) {
            System.err.println("Error saving user profile: " + e.getMessage());
        }
    }

    private void loadUserProfile() {
        File file = new File(PROFILE_FILE);
        if (!file.exists()) {
            userProfile = null;
            return;
        }
        
        try {
            userProfile = mapper.readValue(file, UserProfile.class);
        } catch (IOException e) {
            System.err.println("Error loading user profile: " + e.getMessage());
            userProfile = null;
        }
    }
}