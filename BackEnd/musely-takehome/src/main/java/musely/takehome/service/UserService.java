package musely.takehome.service;

import musely.takehome.dal.model.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserService {

    private static Map<Long, User> db = new HashMap<>();

    public UserService() {
        try {
            loadDataFromDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }

    public User insert(User user) {
        db.put(user.getId(), user);
        return user;
    }

    public User getById(Long id) {
        return db.get(id);
    }

    public List<User> getAll() {
        return db.values().stream().collect(Collectors.toList());
    }

    public boolean exists(Long id) {
        return db.containsKey(id);
    }

    public void updateUserBalance(User user) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Establish a database connection
            connection = DatabaseConnection.getConnection();

            // Prepare the SQL statement
            String query = "UPDATE users SET balance = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setBigDecimal(1, user.getBalance());
            statement.setLong(2, user.getId());

            // Execute the update statement
            statement.executeUpdate();

            // Update the balance in the hashmap as well
            db.put(user.getId(), user);
        } finally {
            // Close the statement and connection in a finally block to ensure they are always closed
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void insertUser(User user, Connection connection) throws SQLException {
        String query = "INSERT INTO users (id, name, lastname, email, balance) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the parameter values for the SQL statement
            statement.setLong(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getLastname());
            statement.setString(4, user.getEmail());
            statement.setBigDecimal(5, user.getBalance());

            // Execute the insert statement
            statement.executeUpdate();
        }
    }

    private void loadDataFromDatabase() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();

            // Prepare and execute the SQL query
            String query = "SELECT * FROM users";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            // Process the query results
            while (resultSet.next()) {
                // Retrieve the user data from the result set
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                BigDecimal balance = resultSet.getBigDecimal("balance");

                // Create a User object with the retrieved data
                User user = new User(id, name, lastname, email, balance);

                // Store the user in the data structure (e.g., hashmap)
                db.put(id, user);
            }
        } finally {
            // Close the result set, statement, and connection in a finally block to ensure proper cleanup
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
