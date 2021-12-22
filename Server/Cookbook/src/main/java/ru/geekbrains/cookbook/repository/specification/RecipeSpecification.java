package ru.geekbrains.cookbook.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.domain.HashTag;
import ru.geekbrains.cookbook.domain.Recipe;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification {
    public static Specification<Recipe> recipeFilter(Long categoryId, String titleRegex, Integer prepareTime, List<String> tags, Long authorId){
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
                if(prepareTime != null)
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("prepareTime"), prepareTime));
                if(tags != null && !tags.isEmpty()){
                    Join<Recipe, HashTag> recipeTag = root.join("tags");
                    CriteriaBuilder.In<String> inTagClause = criteriaBuilder.in(criteriaBuilder.function("upper", String.class, recipeTag.get("name")));
                    for (String tag : tags) {
                        inTagClause.value(tag.toUpperCase());
                    }
                    predicates.add(inTagClause);
                }
                if(authorId != null)
                    predicates.add(criteriaBuilder.equal(root.get("user").get("id"), authorId));
                return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
            }
        };
    }
}
