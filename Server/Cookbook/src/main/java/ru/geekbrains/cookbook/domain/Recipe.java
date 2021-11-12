package ru.geekbrains.cookbook.domain;

import ru.geekbrains.cookbook.auth.*;
import javax.persistence.*;

@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "recipe", nullable = false)
    private String recipe;

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

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
