import musely.takehome.dal.model.User;
import musely.takehome.rest.CreditEndpoint;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CreditEndpointTest {
    private CreditEndpoint creditEndpoint;
    private UserService userServiceMock;

    @Before
    public void setUp() {
        userServiceMock = mock(UserService.class);
        creditEndpoint = new CreditEndpoint(userServiceMock);
    }

    @Test
    public void testCredit_ValidData_ReturnsUpdatedUser() throws SQLException, ClassNotFoundException {
        // Arrange
        Long userId = 1L;
        BigDecimal creditAmount = new BigDecimal("50.00");
        User existingUser = new User(userId, "John", "Doe", "john.doe@example.com", new BigDecimal("100.00"));
        User updatedUser = new User(userId, "John", "Doe", "john.doe@example.com", new BigDecimal("150.00"));
        when(userServiceMock.getById(userId)).thenReturn(existingUser);
        when(userServiceMock.updateUserBalance(updatedUser)).thenReturn(true);

        // Act
        Response response = creditEndpoint.credit(userId, creditAmount);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedUser, response.getEntity());
    }

    @Test
    public void testCredit_UserNotFound_ReturnsNotFound() throws SQLException, ClassNotFoundException {
        // Arrange
        Long userId = 1L;
        BigDecimal creditAmount = new BigDecimal("50.00");
        when(userServiceMock.getById(userId)).thenReturn(null);

        // Act
        Response response = creditEndpoint.credit(userId, creditAmount);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    // Add more test methods to cover other scenarios and functionalities
}
