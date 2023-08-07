package musely.takehome.rest;

import musely.takehome.dal.model.User;
import musely.takehome.service.DatabaseConnection;
import musely.takehome.service.UserService;
import javax.ws.rs.core.Response;
import javassist.NotFoundException;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UserEndpoint {
	
	private UserService userService = new UserService();

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        // List to store all users
        List<User> userList = new ArrayList<>();
        

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            // Iterate over the result set and retrieve user data
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                BigDecimal balance = resultSet.getBigDecimal("balance");

                // Create User object and add it to the list
                User user = new User(id, name, lastname, email, balance);
                userList.add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Handle and log the exception appropriately
            throw new InternalServerErrorException("Failed to fetch users. Please try again later.", e);
        }

        // Return the list of users with CORS headers
        return Response.ok(userList)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) throws NotFoundException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String lastname = resultSet.getString("lastname");
                    String email = resultSet.getString("email");
                    BigDecimal balance = resultSet.getBigDecimal("balance");

                    // Create and return the User object with the specified ID
                    User user = new User(id, name, lastname, email, balance);
                    return Response.ok(user)
                            .header("Access-Control-Allow-Origin", "*")
                            .header("Access-Control-Allow-Methods", "GET")
                            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                            .build();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Handle and log the exception appropriately
            throw new InternalServerErrorException("Failed to fetch user. Please try again later.", e);
        }

        // User with the specified ID is not found, throw NotFoundException
        throw new NotFoundException("User not found.");
    }
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMultipleUsers(List<User> users) {
        // List to store successfully created users
        List<User> createdUsers = new ArrayList<>();
        Connection connection = null;

        try {
            // Validate inputs
            if (users == null || users.isEmpty()) {
                // Return a bad request response if no users are provided
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No users provided.")
                        .build();
            }

            // Get a database connection and start a transaction
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            for (User user : users) {
                // Perform validations on each user object
                if (!UserValidator.isValidUser(user)) {
                    // If validation fails, rollback the transaction and return a bad request response
                    connection.rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Invalid user data: " + user)
                            .build();
                }

                // Insert the user into the database
                userService.insertUser(user, connection);

                // Add the user to the list of created users
                createdUsers.add(user);
            }

            // Commit the transaction
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();

            // Return a success response with the created users
            //return Response.status(Response.Status.OK).entity(createdUsers).build();
            // Return the response with CORS headers
            // Return the list of users with CORS headers
            return Response.ok(createdUsers)
            	    .header("Access-Control-Allow-Origin", "*")
            	    .header("Access-Control-Allow-Methods", "POST")
            	    .build(); 
        } catch (SQLException | ClassNotFoundException e) {
            // If an exception occurs, rollback the transaction and handle the error
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ex) {
                    // Log the rollback failure
                }
            }
            // Log the exception and return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating users: " + e.getMessage())
                    .build();
        }
    }

    
    @POST
    @Path("/{id}/credit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response creditBalance(@PathParam("id") Long id, User u1) throws ClassNotFoundException {
        try {
            // Get the user by ID
            User user = userService.getById(id);
            
            // Check if user exists
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }

            synchronized (user) {
                BigDecimal currentBalance = user.getBalance();
                BigDecimal balance = u1.getBalance(); // Get the amount from u1

                // Calculate the new balance after crediting
                BigDecimal newBalance = currentBalance.add(balance);
                user.setBalance(newBalance);

                // Update the user's balance in the database
                userService.updateUserBalance(user);
            }

            // Return success response with the updated user
            //return Response.status(Response.Status.OK).entity(user).build();
            return Response.status(Response.Status.OK)
                    .entity(user)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (SQLException e) {
            // Handle the database or other errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update balance").build();
        }
    }


    @POST
    @Path("/{id}/debit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response debitBalance(@PathParam("id") Long id, User u1) throws ClassNotFoundException {
        try {
            // Get the user by ID
            User user = userService.getById(id);

            // Check if user exists
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }

            synchronized (user) {
                BigDecimal currentBalance = user.getBalance();
                BigDecimal amount = u1.getBalance(); // Get the amount from u1
                
                // Calculate the new balance after debiting
                BigDecimal newBalance = currentBalance.subtract(amount);

                // Check if the new balance is negative (insufficient balance)
                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Insufficient balance").build();
                }

                user.setBalance(newBalance);

                // Update the user's balance in the database
                userService.updateUserBalance(user);
            }

            // Return success response with the updated user
           // return Response.status(Response.Status.OK).entity(user).build();
            // Return the response with CORS headers
            return Response.status(Response.Status.OK)
                    .entity(user)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (SQLException e) {
            // Handle the database or other errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update balance").build();
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
            // Check the format of the email using regex 
            String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            return email != null && email.matches(emailPattern);
        }
    }

    } 


