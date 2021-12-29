package ru.geekbrains.cookbook.listener.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.UserRating;
import ru.geekbrains.cookbook.event.RatingChangedEvent;

@Component("emailRatingChangedListener")
@RequiredArgsConstructor
public class RatingChangedListener implements ApplicationListener<RatingChangedEvent> {
    private final JavaMailSender mailSender;
    private final Environment env;

    @Override
    public void onApplicationEvent(RatingChangedEvent event) {
        sendMessage(event);
    }

    private void sendMessage(RatingChangedEvent event) {
        SimpleMailMessage email = constructEmailMessage(event);
        try{
            mailSender.send(email);
        }
        catch(MailSendException ignored){

        }

    }

    private SimpleMailMessage constructEmailMessage(RatingChangedEvent event) {
        String username = event.getUsername();
        String recipeTitle = event.getRecipeTitle();
        UserRating rating = event.getRating();
        String recipientAddress = event.getEmail();
        String subject = "Ваш рецепт оценили";
        String message = String.format("Пользователь %s оценил ваш рецепт \"%s\" на %d балла.", username, recipeTitle, rating.getRate());
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        return email;
    }


}

