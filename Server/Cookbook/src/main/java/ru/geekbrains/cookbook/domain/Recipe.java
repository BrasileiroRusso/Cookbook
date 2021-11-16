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
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "recipe", nullable = false)
    private String recipe;

    @Column(name = "imagepath")
    private String imagePath;

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
}
