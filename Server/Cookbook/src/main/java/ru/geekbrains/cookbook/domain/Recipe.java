package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.*;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "recipe")
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
    @JsonManagedReference
    private Set<RecipeIngredient> ingredients;

    @Column(name = "recipe", nullable = false)
    private String recipe;

    @Column(name = "imagepath")
    private String imagePath;

    @ElementCollection
    @CollectionTable(name = "recipe_step")
    @MapKeyColumn(name = "step_num")
    private Map<Integer, RecipeStep> steps = new HashMap<Integer, RecipeStep>();

    public Recipe(){
    }

    public Long getId() {
        return id;
    }

    public String getRecipe() {
        return recipe;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Set<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public Map<Integer, RecipeStep> getSteps() {
        return steps;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setIngredients(Set<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(Map<Integer, RecipeStep> steps) {
        this.steps = steps;
    }

    @Embeddable
    @NoArgsConstructor
    @Data
    public static class RecipeStep{
        @Column(name = "description", nullable = false)
        private String description;

        @Column(name = "image_path")
        private String imagePath;
    }
}
