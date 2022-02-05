package hr.vgsoft.cookbook.service;

import hr.vgsoft.cookbook.domain.User;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

public abstract class AbstractMailService implements MailService  {
	
	 private final Logger log = LoggerFactory.getLogger(DefaultMailService.class);

	    private static final String USER = "user";

	    private static final String BASE_URL = "baseUrl";

	    protected final JHipsterProperties jHipsterProperties;

	    protected final MessageSource messageSource;

	    private final SpringTemplateEngine templateEngine;

	    public AbstractMailService(
	            JHipsterProperties jHipsterProperties,
	            MessageSource messageSource,
	            SpringTemplateEngine templateEngine
	        ) {
	            this.jHipsterProperties = jHipsterProperties;
	            this.messageSource = messageSource;
	            this.templateEngine = templateEngine;
	        }
	 
	    abstract void sendEmail(String to, String subject, String content, boolean isMultipart,
	        boolean isHtml);
	    

	    void sendEmailFromTemplate(User user, String templateName, String titleKey) {
	        if (user.getEmail() == null) {
	            log.debug("Email doesn't exist for user '{}'", user.getLogin());
	            return;
	        }
	        Locale locale = Locale.forLanguageTag(user.getLangKey());
	        Context context = new Context(locale);
	        context.setVariable(USER, user);
	        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
	        String content = templateEngine.process(templateName, context);
	        String subject = messageSource.getMessage(titleKey, null, locale);
	        sendEmail(user.getEmail(), subject, content, false, true);
	    }
	    

	    @Override
	    public void sendActivationEmail(User user) {
	        log.debug("Sending activation email to '{}'", user.getEmail());
	        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
	    }

	    @Override
	    public void sendCreationEmail(User user) {
	        log.debug("Sending creation email to '{}'", user.getEmail());
	        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
	    }

	    @Override
	    public void sendPasswordResetMail(User user) {
	        log.debug("Sending password reset email to '{}'", user.getEmail());
	        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
	    }

}
