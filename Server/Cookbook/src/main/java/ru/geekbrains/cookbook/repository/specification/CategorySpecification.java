package ru.geekbrains.cookbook.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.cookbook.domain.Category;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {
    public static Specification<Category> categoryFilter(Long parentId, Boolean childs){
        return new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(parentId != null)
                    predicates.add(criteriaBuilder.equal(root.get("parentCategory").get("id"), parentId));
                if(childs != null){
                    Subquery<Category> subquery = criteriaQuery.subquery(Category.class);
                    Root<Category> child = subquery.from(Category.class);
                    subquery.select(child).where(criteriaBuilder.equal(child.get("parentCategory"), root));
                    if(childs)
                        predicates.add(criteriaBuilder.exists(subquery));
                    else
                        predicates.add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
                }
                return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
            }
        };
    }
}
