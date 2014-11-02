package scratch.jersey;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scratch.user.User;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Invocation.Builder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JerseyUsersTest {

    private static final Long ID = 1L;

    @Mock
    private User user;

    @Mock
    private WebTarget target;

    @Mock
    private Builder builder;

    @Mock
    private Response response;

    @Mock
    private Map map;

    @Mock
    private List<User> users;

    @Test
    public void I_can_create_a_user() {

        when(target.path("users")).thenReturn(target);
        when(target.request(APPLICATION_JSON_TYPE)).thenReturn(builder);

        when(builder.post(entity(user, APPLICATION_JSON_TYPE))).thenReturn(response);

        when(response.readEntity(Map.class)).thenReturn(map);

        when(map.get("id")).thenReturn(ID);

        final Long id = new JerseyUsers(target).create(user);

        assertEquals("the created id should be correct.", ID, id);
    }

    @Test
    public void I_can_retrieve_a_user() {

        when(target.path("users")).thenReturn(target);
        when(target.path(ID.toString())).thenReturn(target);
        when(target.request(APPLICATION_JSON_TYPE)).thenReturn(builder);

        when(builder.get(User.class)).thenReturn(user);

        final User actual = new JerseyUsers(target).retrieve(ID);

        assertEquals("the retrieved id should be correct.", user, actual);
    }

    @Test
    public void I_can_retrieve_all_users() {

        when(target.path("users")).thenReturn(target);
        when(target.request(APPLICATION_JSON_TYPE)).thenReturn(builder);

        when(builder.get(new GenericType<List<User>>() {})).thenReturn(users);

        final Iterable<User> actual = new JerseyUsers(target).retrieve();

        assertEquals("the retrieved users should be correct.", users, actual);
    }

    @Test
    public void I_can_update_a_user() {

        when(user.getId()).thenReturn(ID);

        when(target.path("users")).thenReturn(target);
        when(target.path(ID.toString())).thenReturn(target);
        when(target.request(APPLICATION_JSON_TYPE)).thenReturn(builder);

        new JerseyUsers(target).update(user);

        verify(builder).put(entity(user, APPLICATION_JSON_TYPE));
    }

    @Test
    public void I_can_delete_a_user() {

        when(target.path("users")).thenReturn(target);
        when(target.path(ID.toString())).thenReturn(target);
        when(target.request(APPLICATION_JSON_TYPE)).thenReturn(builder);

        new JerseyUsers(target).delete(ID);

        verify(builder).delete();
    }

    @Test
    public void I_can_delete_all_users() {

        when(target.path("users")).thenReturn(target);
        when(target.request(APPLICATION_JSON_TYPE)).thenReturn(builder);

        new JerseyUsers(target).deleteAll();

        verify(builder).delete();
    }
}
