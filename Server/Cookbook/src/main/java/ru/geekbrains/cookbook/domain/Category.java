package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"childCategorySet"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Category> childCategorySet;

}
