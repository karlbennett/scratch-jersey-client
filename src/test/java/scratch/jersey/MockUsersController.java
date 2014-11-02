package scratch.jersey;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import scratch.user.User;
import scratch.user.Users;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/users")
public class MockUsersController {

    private Users users;

    public void setUsers(Users users) {
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

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Map<String, String> notFound(NotFoundException e) {

        return singletonMap("message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Map<String, String> badRequest(BadRequestException e) {

        return singletonMap("message", e.getMessage());
    }
}                                        
