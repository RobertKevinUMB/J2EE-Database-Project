import musely.takehome.dal.model.User;
import musely.takehome.service.DatabaseConnection;
import musely.takehome.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class CreateEndpoint {
    private UserService userService;

    public CreateEndpoint(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMultipleUsers(List<User> users) throws ClassNotFoundException, SQLException {

        // List to store successfully created users
        List<User> createdUsers = new ArrayList<>();
        // Start a transaction
        Connection connection = DatabaseConnection.getConnection();

        try {

            connection.setAutoCommit(false);

            for (User user : users) {
                // Perform validations on each user object
                if (!UserValidator.isValidUser(user)) {
                    // If validation fails, throw an exception or return an error response
                    connection.rollback();
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid user data").build();
                }

                // Insert the user into the database
                insertUser(user, connection);

                // Add the user to the list of created users
                createdUsers.add(user);
            }

            // Commit the transaction
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();


            // Return a success response with the created users
            return Response.status(Response.Status.OK).entity(createdUsers).build();
        } catch (Exception e) {
            // Rollback the transaction
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();

            // Return an error response indicating that none of the users were added
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating users").build();
        }
    }

    private void insertUser(User user, Connection connection) throws SQLException {

        String query = "INSERT INTO users (id, name, lastname, email, balance) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getLastname());
            statement.setString(4, user.getEmail());
            statement.setBigDecimal(5, user.getBalance());

            statement.executeUpdate();
        }
    }

    private static class UserValidator {
        public static boolean isValidUser(User user) {
            Long id = user.getId();
            String name = user.getName();
            String lastname = user.getLastname();
            String email = user.getEmail();
            BigDecimal balance = user.getBalance();

            // Check if id, lastname, balance, and email are valid
            if (id != null && id > 0 && name != null && !name.isEmpty() &&
                    lastname != null && !lastname.isEmpty() &&
                    balance != null && balance.compareTo(BigDecimal.ZERO) > 0 &&
                    isValidEmail(email)) {
                // Additional validation logic can be added here if needed

                // Return true if all validations pass
                return true;
            }

            // Return false if any validation fails
            return false;
        }

        private static boolean isValidEmail(String email) {
            // Check the format of the email using regex or email validation libraries
            // For example, using regex:
            String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            return email != null && email.matches(emailPattern);
        } } }
           
