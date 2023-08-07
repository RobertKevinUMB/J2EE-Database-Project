import musely.takehome.dal.model.User;
import musely.takehome.rest.CreateEndpoint;
import musely.takehome.service.UserService;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CreateEndpointTest {

    private CreateEndpoint createEndpoint;
    private UserService userServiceMock;

    @Before
    public void setUp() {
        userServiceMock = mock(UserService.class);
        createEndpoint = new CreateEndpoint(userServiceMock);
    }

    @Test
    public void testCreateMultipleUsers_ValidData_ReturnsCreatedUsers() throws SQLException, ClassNotFoundException {
        // Arrange
        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", new BigDecimal("100.00"));
        User user2 = new User(2L, "Jane", "Smith", "jane.smith@example.com", new BigDecimal("200.00"));
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userServiceMock.createMultipleUsers(users)).thenReturn(users);

        // Act
        Response response = createEndpoint.createMultipleUsers(users);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(users, response.getEntity());
        verify(userServiceMock, times(1)).createMultipleUsers(users);
    }

    @Test
    public void testCreateMultipleUsers_InvalidData_ReturnsBadRequest() throws SQLException, ClassNotFoundException {
        // Arrange
        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", new BigDecimal("100.00"));
        User user2 = new User(2L, "Jane", "", "jane.smith@example.com", new BigDecimal("200.00"));
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        // Act
        Response response = createEndpoint.createMultipleUsers(users);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Invalid user data", response.getEntity());
        verify(userServiceMock, never()).createMultipleUsers(users);
    }

    // Add more test methods to cover other scenarios and functionalities
}
