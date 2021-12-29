package ru.geekbrains.cookbook.domain;

import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.*;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "recipe")
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

    @OneToOne(fetch = FetchType.EAGER, optional = true, cascade = {CascadeType.REMOVE})
    @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "recipe_id")
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

    @Column(name = "prepare_time")
    private Integer prepareTime;

    @Column(name = "comment")
    private String comment;

    @Embeddable
    @NoArgsConstructor
    public static class RecipeStep{
        @Column(name = "description", nullable = false)
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public RecipeRating getRating() {
        return rating;
    }

    public void setRating(RecipeRating rating) {
        this.rating = rating;
    }

    public Set<HashTag> getTags() {
        return tags;
    }

    public void setTags(Set<HashTag> tags) {
        this.tags = tags;
    }

    public Map<Integer, RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(Map<Integer, RecipeStep> steps) {
        this.steps = steps;
    }

    public Integer getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(Integer prepareTime) {
        this.prepareTime = prepareTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
