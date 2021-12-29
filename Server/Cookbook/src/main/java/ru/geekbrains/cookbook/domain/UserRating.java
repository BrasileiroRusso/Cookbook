package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.User;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_rating")
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"recipe", "user", "comment", "rate"})
public class UserRating {
    @EmbeddedId
    @JsonIgnore
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "rate", nullable = false)
    private int rate;

    @Column(name = "comment")
    private String comment;

    public UserRating(Long recipeId, Long userId){
        id = new Id(recipeId, userId);
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Id implements Serializable {
        @Column(name = "recipe_id")
        private Long recipeId;

        @Column(name = "user_id")
        private Long userId;
    }

}
