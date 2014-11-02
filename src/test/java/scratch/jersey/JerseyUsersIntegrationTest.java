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
import scratch.user.User;
import scratch.user.Users;

import javax.ws.rs.client.ClientBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestUsersConfiguration.class)
@WebAppConfiguration("classpath:")
@IntegrationTest({"server.port=0", "management.port=0"})
public class JerseyUsersIntegrationTest {

    private static final Long ID = 1L;
    private static final String EMAIL = "test@email.com";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";
    private static final String PHONE_NUMBER = "5551234";

    private static final Integer NUMBER = 2;
    private static final String STREET = "This Road";
    private static final String SUBURB = "That Suburb";
    private static final String CITY = "Your City";
    private static final String POSTCODE = "ABC123";

    private static final User USER = user();

    private static User user() {

        final User user = new User(EMAIL, FIRST_NAME, LAST_NAME, PHONE_NUMBER,
                new Address(NUMBER, STREET, SUBURB, CITY, POSTCODE));
        user.setId(ID);

        return user;
    }

    @Autowired
    private Users mockUsers;

    @Value("${local.server.port}")
    private int port;

    private Users users;

    @Before
    public void setUp() {

        users = new JerseyUsers(ClientBuilder.newClient().target(format("http://localhost:%d/", port)));
    }

    @Test
    public void I_can_create_a_user() {

        when(mockUsers.create(USER)).thenReturn(ID);

        assertEquals("the correct user id should be returned.", ID, users.create(USER));
    }

    @Test
    public void I_can_retrieve_a_user() {

        when(mockUsers.retrieve(ID)).thenReturn(USER);

        assertEquals("the correct user should be returned.", USER, users.retrieve(ID));
    }

    @Test
    public void I_can_retrieve_all_users() {

        final Set<User> expected = new HashSet<>();
        expected.add(USER);

        when(mockUsers.retrieve()).thenReturn(expected);

        assertEquals("the correct user should be returned.", expected, toSet(users.retrieve()));
    }

    @Test
    public void I_can_update_a_user() {

        users.update(USER);

        verify(mockUsers).update(USER);
    }

    @Test
    public void I_can_delete_a_user() {

        users.delete(ID);

        verify(mockUsers).delete(ID);
    }

    private static <T> Set<T> toSet(Iterable<T> iterable) {

        final Set<T> set = new HashSet<>();

        for (T item : iterable) {
            set.add(item);
        }

        return set;
    }


}
