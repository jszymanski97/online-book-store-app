package mate.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import mate.project.validation.CoverImage;

public record CreateBookRequestDto(
        @NotNull
        String title,
        @NotNull
        String author,
        @NotNull
        String isbn,
        @NotNull
        @Min(0)
        BigDecimal price,
        @Size(min = 10, max = 1000)
        String description,
        @CoverImage
        String coverImage
) {}
