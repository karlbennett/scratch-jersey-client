package scratch.jersey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import scratch.user.User;
import scratch.user.Users;

import javax.validation.Valid;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/users")
public class TestUsersController {

    private final Users users;

    @Autowired
    public TestUsersController(Users users) {
        this.users = users;
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Map<String, Long> create(@Valid @RequestBody User user) {

        return singletonMap("id", users.create(user));
    }

    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public User retrieve(@PathVariable Long id) {

        return users.retrieve(id);
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public Iterable<User> retrieve() {

        return users.retrieve();
    }

    @RequestMapping(value = "/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody final User user) {

        user.setId(id);

        users.update(user);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {

        users.delete(id);
    }
}
