package musely.takehome.rest;

import musely.takehome.service.AuthenticationService;
import musely.takehome.dal.model.AuthResponse;
import musely.takehome.dal.model.UserCredentials;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/auth")
public class AuthenticationEndpoint {
    private AuthenticationService authService;

    public AuthenticationEndpoint(AuthenticationService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredentials credentials) {
        try {
            String username = credentials.getUsername();
            String password = credentials.getPassword();

            // Validate the credentials against the user database
            if (authService.authenticateUser(username, password)) {
                // Generate an authentication token for the authenticated user
                String authToken = authService.generateAuthToken(username);

                // Return the authentication token as a response
                return Response.status(Response.Status.OK)
                        .entity(new AuthResponse(authToken))
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid credentials")
                        .build();
            }
        } catch (Exception e) {
            // Handle the exception and return an error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred during authentication")
                    .build();
        }
    }
}
