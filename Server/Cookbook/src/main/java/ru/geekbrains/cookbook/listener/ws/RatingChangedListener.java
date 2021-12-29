package ru.geekbrains.cookbook.listener.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.configuration.properties.WsProperties;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.UserRating;
import ru.geekbrains.cookbook.event.RatingChangedEvent;

@Component("wsRatingChangedListener")
@RequiredArgsConstructor
public class RatingChangedListener implements ApplicationListener<RatingChangedEvent> {
    private final WsProperties props;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onApplicationEvent(RatingChangedEvent event) {
        UserRating rating = event.getRating();
        //simpMessagingTemplate.convertAndSend(props.getUserPrefix() + '/' + author.getUsername() + props.getRatings(), rating);
    }

}



