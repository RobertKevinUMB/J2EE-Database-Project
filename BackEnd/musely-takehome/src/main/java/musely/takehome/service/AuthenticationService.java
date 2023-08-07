package musely.takehome.service;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, String> userDatabase; // Simulating a user database

    public AuthenticationService() {
        // Initialize the user database with some sample users
        userDatabase = new HashMap<>();
        userDatabase.put("musely", "password1");
        userDatabase.put("user2", "password2");
        userDatabase.put("user3", "password3");
    }

    public boolean authenticateUser(String username, String password) {
        // Check if the provided username and password match a user in the database
        String storedPassword = userDatabase.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public String generateAuthToken(String username) {
        // Generate and return an authentication token for the authenticated user
        // Here, we'll simply return the username as the token
        return username;
    }
}
