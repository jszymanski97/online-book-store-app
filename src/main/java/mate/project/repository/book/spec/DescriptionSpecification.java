package mate.project.repository.book.spec;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import mate.project.model.Book;
import mate.project.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DescriptionSpecification implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "description";
    }

    public Specification<Book> getSpecification(Object[] params) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = Arrays.stream(params)
                    .map(param -> criteriaBuilder.like(criteriaBuilder
                            .lower(root.get("description")), "%"
                            + param.toString().toLowerCase() + "%"))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }
}
