package scratch.jersey;

import scratch.user.User;
import scratch.user.Users;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * An implementation of the {@link Users} interface that uses Jersey to call a locally running {@code scratch-*-rest}
 * application.
 *
 * @author Karl Bennett
 */
public class JerseyUsers implements Users {

    private static final String USERS = "users";

    private final WebTarget target;

    public JerseyUsers(WebTarget target) {
        this.target = target;
    }

    @Override
    public Long create(User user) {

        final Response response = target.path(USERS).request(APPLICATION_JSON_TYPE)
                .post(entity(user, APPLICATION_JSON_TYPE));

        final Map body = response.readEntity(Map.class);

        return Long.valueOf(body.get("id").toString());
    }

    @Override
    public User retrieve(Long id) {

        return target.path(USERS).path(id.toString()).request(APPLICATION_JSON_TYPE).get(User.class);
    }

    @Override
    public Iterable<User> retrieve() {

        return target.path(USERS).request(APPLICATION_JSON_TYPE).get(new GenericType<List<User>>(){});
    }

    @Override
    public void update(User user) {

        target.path(USERS).path(user.getId().toString()).request(APPLICATION_JSON_TYPE)
                .put(entity(user, APPLICATION_JSON_TYPE));
    }

    @Override
    public void delete(Long id) {

        target.path(USERS).path(id.toString()).request(APPLICATION_JSON_TYPE).delete();
    }

    @Override
    public void deleteAll() {

        target.path(USERS).request(APPLICATION_JSON_TYPE).delete();
    }
}
