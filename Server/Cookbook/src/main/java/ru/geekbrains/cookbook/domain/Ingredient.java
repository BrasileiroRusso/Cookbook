package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ingredient")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"childIngredients"})
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brief_name", nullable = false)
    private String briefName;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Ingredient parentIngredient;

    @Column(name = "is_group")
    @JsonIgnore
    private Boolean group;

    @OneToMany(mappedBy = "parentIngredient", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Ingredient> childIngredients;

}
