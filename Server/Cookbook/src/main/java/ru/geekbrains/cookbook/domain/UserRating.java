package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.User;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_rating")
@Data
@NoArgsConstructor
public class UserRating {
    @EmbeddedId
    @JsonIgnore
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "rate")
    private int rate;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "recipe_id")
        private Long recipeId;

        @Column(name = "user_id")
        private Long userId;
    }

}
