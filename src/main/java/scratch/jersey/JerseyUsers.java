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
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

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

        checkForBadRequest(response);
        checkForCorrectStatus(response, CREATED.getStatusCode());

        final Map body = response.readEntity(Map.class);

        return Long.valueOf(body.get("id").toString());
    }

    @Override
    public User retrieve(Long id) {

        final Response response = target.path(USERS).path(id.toString()).request(APPLICATION_JSON_TYPE).get();

        checkForNotFound(response);
        checkForBadRequest(response);
        checkForCorrectStatus(response, OK.getStatusCode());

        return response.readEntity(User.class);
    }

    @Override
    public Iterable<User> retrieve() {

        final Response response = target.path(USERS).request(APPLICATION_JSON_TYPE).get();

        checkForBadRequest(response);
        checkForCorrectStatus(response, OK.getStatusCode());

        return response.readEntity(new GenericType<List<User>>() {
        });
    }

    @Override
    public void update(User user) {

        final Response response = target.path(USERS).path(user.getId().toString()).request(APPLICATION_JSON_TYPE)
                .put(entity(user, APPLICATION_JSON_TYPE));

        checkForNotFound(response);
        checkForBadRequest(response);
        checkForCorrectStatus(response, NO_CONTENT.getStatusCode());
    }

    @Override
    public void delete(Long id) {

        final Response response = target.path(USERS).path(id.toString()).request(APPLICATION_JSON_TYPE).delete();

        checkForNotFound(response);
        checkForBadRequest(response);
        checkForCorrectStatus(response, NO_CONTENT.getStatusCode());
    }

    @Override
    public void deleteAll() {

        final Response response = target.path(USERS).request(APPLICATION_JSON_TYPE).delete();

        checkForBadRequest(response);
        checkForCorrectStatus(response, NO_CONTENT.getStatusCode());
    }

    private static void checkForNotFound(Response response) {

        checkForStatus(response, NOT_FOUND.getStatusCode(), CREATOR_ILLEGAL_STATE);
    }

    private static void checkForBadRequest(Response response) {

        checkForStatus(response, BAD_REQUEST.getStatusCode(), CREATOR_ILLEGAL_ARGUMENT);
    }

    private static <T extends RuntimeException> void checkForStatus(Response response, int status,
                                                                    ExceptionCreator<T> e) {

        if (response.getStatus() == status) {

            final Map body = response.readEntity(Map.class);

            throw e.create(body.get("message").toString());
        }
    }

    private static void checkForCorrectStatus(Response response, int status) {

        if (response.getStatus() == status) {
            return;
        }

        final Map body = response.readEntity(Map.class);

        throw new RuntimeException(body.get("message").toString());
    }

    private interface ExceptionCreator<T extends Throwable> {

        T create(String message);
    }

    private static final ExceptionCreator<IllegalStateException> CREATOR_ILLEGAL_STATE =
            new ExceptionCreator<IllegalStateException>() {
                @Override
                public IllegalStateException create(String message) {
                    return new IllegalStateException(message);
                }
            };

    private static final ExceptionCreator<IllegalArgumentException> CREATOR_ILLEGAL_ARGUMENT =
            new ExceptionCreator<IllegalArgumentException>() {
                @Override
                public IllegalArgumentException create(String message) {
                    return new IllegalArgumentException(message);
                }
            };
}
