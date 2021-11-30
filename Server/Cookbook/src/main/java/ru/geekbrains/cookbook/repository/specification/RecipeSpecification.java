package ru.geekbrains.cookbook.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.cookbook.domain.Recipe;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification {
    public static Specification<Recipe> recipeFilter(Long categoryId, String titleRegex){
        return new Specification<Recipe>() {
            @Override
            public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(categoryId != null)
                    predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
                if(titleRegex != null && !titleRegex.isEmpty()){
                    String sqlTemplate = "%" + titleRegex.toUpperCase() + "%";
                    predicates.add(criteriaBuilder.like(criteriaBuilder.function("upper", String.class, root.get("title")), sqlTemplate));
                }
                return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
            }
        };
    }
}
