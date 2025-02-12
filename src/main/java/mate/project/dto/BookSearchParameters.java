package mate.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record BookSearchParameters(
        @Size(min = 1, max = 255)
        String[] titles,
        @Size(min = 1, max = 255)
        String[] authors,
        String[] isbn,
        @DecimalMin("0.0")
        BigDecimal minPrice,
        @DecimalMin("0.0")
        BigDecimal maxPrice,
        @Size(min = 1, max = 255)
        String[] descriptionKeyword
) {}
