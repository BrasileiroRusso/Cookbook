package ru.geekbrains.cookbook.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.event.RegistrationCompletedEvent;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<RegistrationCompletedEvent> {
    private final JavaMailSender mailSender;
    private final Environment env;

    @Override
    public void onApplicationEvent(RegistrationCompletedEvent event) {
        confirmRegistration(event);
    }

    private void confirmRegistration(RegistrationCompletedEvent event) {
        User user = event.getUser();
        SimpleMailMessage email = constructEmailMessage(event, user);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(RegistrationCompletedEvent event, User user) {
        String recipientAddress = user.getEmail();
        String subject = "Подтверждение регистрации";
        String message = "Вы успешно зарегистрировались в приложении Cookbook. Приятного аппетита!";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        //email.setFrom(env.getProperty("support.email"));
        email.setFrom("cookbookjava@gmail.com");
        return email;
    }


}

