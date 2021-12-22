package ru.geekbrains.cookbook.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.*;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "recipe")
@Data
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    //@JsonManagedReference
    private Set<RecipeIngredient> ingredients;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @PrimaryKeyJoinColumn
    private RecipeRating rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<HashTag> tags;

    @ElementCollection
    @CollectionTable(name = "recipe_step")
    @MapKeyColumn(name = "step_num")
    private Map<Integer, RecipeStep> steps = new HashMap<>();

    @Embeddable
    @NoArgsConstructor
    @Data
    public static class RecipeStep{
        @Column(name = "description", nullable = false)
        private String description;

        public String getDescription() {
            return description;
        }
    }

}
