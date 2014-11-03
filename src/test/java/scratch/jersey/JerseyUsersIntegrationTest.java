package scratch.jersey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scratch.user.Address;
import scratch.user.Id;
import scratch.user.User;
import scratch.user.Users;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.ClientBuilder;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockUsersConfiguration.class)
@WebAppConfiguration("classpath:")
@IntegrationTest({"server.port=0", "management.port=0"})
public class JerseyUsersIntegrationTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "test@email.com";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";
    private static final String PHONE_NUMBER = "5551234";

    private static final Long ADDRESS_ID = 2L;
    private static final Integer NUMBER = 3;
    private static final String STREET = "This Road";
    private static final String SUBURB = "That Suburb";
    private static final String CITY = "Your City";
    private static final String POSTCODE = "ABC123";

    private static final User USER = user();

    private static final Id ID = new Id(USER);

    private static final String MESSAGE = "test message";

    private static User user() {

        final User user = new User(EMAIL, FIRST_NAME, LAST_NAME, PHONE_NUMBER,
                new Address(NUMBER, STREET, SUBURB, CITY, POSTCODE));
        user.setId(USER_ID);
        user.getAddress().setId(ADDRESS_ID);

        return user;
    }

    @Autowired
    private MockUsersController controller;

    private Users mockUsers;

    @Value("${local.server.port}")
    private int port;

    private Users users;

    @Before
    public void setUp() {

        mockUsers = mock(Users.class);

        controller.setUsers(mockUsers);

        users = new JerseyUsers(ClientBuilder.newClient().target(format("http://localhost:%d/", port)));
    }

    @Test
    public void I_can_create_a_user() {

        when(mockUsers.create(USER)).thenReturn(ID);

        assertEquals("the correct user id should be returned.", ID, users.create(USER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void I_cannot_create_an_invalid_user() {

        when(mockUsers.create(USER)).thenThrow(new BadRequestException(MESSAGE));

        users.create(USER);
    }

    @Test(expected = RuntimeException.class)
    public void I_cannot_create_a_user_on_a_broken_server() {

        when(mockUsers.create(USER)).thenThrow(new RuntimeException(MESSAGE));

        users.create(USER);
    }

    @Test
    public void I_can_retrieve_a_user() {

        when(mockUsers.retrieve(USER_ID)).thenReturn(USER);

        assertEquals("the correct user should be returned.", USER, users.retrieve(USER_ID));
    }

    @Test(expected = IllegalStateException.class)
    public void I_cannot_retrieve_a_user_that_does_not_exist() {

        when(mockUsers.retrieve(USER_ID)).thenThrow(new NotFoundException(MESSAGE));

        users.retrieve(USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void I_cannot_retrieve_a_user_with_an_invalid_id() {

        when(mockUsers.retrieve(USER_ID)).thenThrow(new BadRequestException(MESSAGE));

        users.retrieve(USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void I_cannot_retrieve_a_user_on_a_broken_server() {

        when(mockUsers.retrieve(USER_ID)).thenThrow(new RuntimeException(MESSAGE));

        users.retrieve(USER_ID);
    }

    @Test
    public void I_can_retrieve_all_users() {

        final Set<User> expected = new HashSet<>();
        expected.add(USER);

        when(mockUsers.retrieve()).thenReturn(expected);

        assertEquals("the correct user should be returned.", expected, toSet(users.retrieve()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void I_cannot_retrieve_invalid_users() {

        when(mockUsers.retrieve()).thenThrow(new BadRequestException(MESSAGE));

        users.retrieve();
    }

    @Test(expected = RuntimeException.class)
    public void I_cannot_retrieve_all_users_on_a_broken_server() {

        when(mockUsers.retrieve()).thenThrow(new RuntimeException(MESSAGE));

        users.retrieve();
    }

    @Test
    public void I_can_update_a_user() {

        users.update(USER);

        verify(mockUsers).update(USER);
    }

    @Test(expected = IllegalStateException.class)
    public void I_can_update_a_user_with_an_invalid_id() {

        doThrow(new NotFoundException(MESSAGE)).when(mockUsers).update(USER);

        users.update(USER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void I_can_update_an_invalid_user() {

        doThrow(new BadRequestException(MESSAGE)).when(mockUsers).update(USER);

        users.update(USER);
    }

    @Test(expected = RuntimeException.class)
    public void I_can_update_a_user_on_a_broken_server() {

        doThrow(new RuntimeException(MESSAGE)).when(mockUsers).update(USER);

        users.update(USER);
    }

    @Test
    public void I_can_delete_a_user() {

        users.delete(USER_ID);

        verify(mockUsers).delete(USER_ID);
    }

    @Test(expected = IllegalStateException.class)
    public void I_cannot_delete_a_user_that_does_not_exist() {

        doThrow(new NotFoundException(MESSAGE)).when(mockUsers).delete(USER_ID);

        users.delete(USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void I_cannot_delete_an_invalid_user() {

        doThrow(new BadRequestException(MESSAGE)).when(mockUsers).delete(USER_ID);

        users.delete(USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void I_cannot_delete_a_user_on_a_broken_server() {

        doThrow(new RuntimeException(MESSAGE)).when(mockUsers).delete(USER_ID);

        users.delete(USER_ID);
    }

    @Test
    public void I_can_delete_all_users() {

        users.deleteAll();

        verify(mockUsers).deleteAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void I_cannot_delete_any_invalid_users() {

        doThrow(new BadRequestException(MESSAGE)).when(mockUsers).deleteAll();

        users.deleteAll();
    }

    @Test(expected = RuntimeException.class)
    public void I_cannot_delete_any_users_on_a_broken_server() {

        doThrow(new RuntimeException(MESSAGE)).when(mockUsers).deleteAll();

        users.deleteAll();
    }

    private static <T> Set<T> toSet(Iterable<T> iterable) {

        final Set<T> set = new HashSet<>();

        for (T item : iterable) {
            set.add(item);
        }

        return set;
    }


}
