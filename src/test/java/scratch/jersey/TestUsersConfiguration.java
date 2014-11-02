package scratch.jersey;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import scratch.user.Users;

import static org.mockito.Mockito.mock;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class TestUsersConfiguration {

    @Bean
    public Users mockUsers() {
        return mock(Users.class);
    }
}
