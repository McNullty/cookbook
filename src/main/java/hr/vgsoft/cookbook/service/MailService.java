package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.User;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    void sendActivationEmail(User user);

    void sendCreationEmail(User user);

    void sendPasswordResetMail(User user);
}
