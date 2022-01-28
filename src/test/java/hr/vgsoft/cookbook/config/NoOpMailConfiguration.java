package hr.vgsoft.cookbook.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import hr.vgsoft.cookbook.service.DefaultMailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoOpMailConfiguration {

    private final DefaultMailService mockMailService;

    public NoOpMailConfiguration() {
        mockMailService = mock(DefaultMailService.class);
        doNothing().when(mockMailService).sendActivationEmail(any());
    }

    @Bean
    public DefaultMailService mailService() {
        return mockMailService;
    }
}
