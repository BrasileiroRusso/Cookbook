package ru.geekbrains.cookbook.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.geekbrains.cookbook.auth.User;

public class RegistrationCompletedEvent extends ApplicationEvent {
    @Getter
    private final User user;

    public RegistrationCompletedEvent(User user) {
        super(user);
        this.user = user;
    }

}

