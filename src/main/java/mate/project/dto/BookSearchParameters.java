package mate.project.dto;

import java.math.BigDecimal;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        String[] isbn,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String[] descriptionKeyword
) {}
