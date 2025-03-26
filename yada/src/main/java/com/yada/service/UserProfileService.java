package com.yada.service;

import com.yada.model.*;
import java.util.*;
import java.time.LocalDate;
import java.io.*;



public class UserProfileService {
    private UserProfile userProfile;
    private static final String PROFILE_FILE = "profile.ser";

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
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PROFILE_FILE))) {
            out.writeObject(userProfile);
        } catch (IOException e) {
            System.err.println("Error saving user profile: " + e.getMessage());
        }
    }

    private void loadUserProfile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PROFILE_FILE))) {
            userProfile = (UserProfile) in.readObject();
        } catch (FileNotFoundException e) {
            // First-time run, no profile exists
            userProfile = null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user profile: " + e.getMessage());
            userProfile = null;
        }
    }
}