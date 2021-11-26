package ru.geekbrains.cookbook.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.domain.Recipe;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification {
    public static Specification<Recipe> recipeFilter(Category category, String titleRegex){
        return new Specification<Recipe>() {
            @Override
            public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(category != null)
                    predicates.add(criteriaBuilder.equal(root.get("category"), category));
                if(titleRegex != null && !titleRegex.isEmpty())
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + titleRegex + "%"));
                return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
            }
        };
    }
}
