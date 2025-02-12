package mate.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.experimental.Accessors;
import mate.project.validation.CoverImage;

@Accessors(chain = true)
public record CreateBookRequestDto(
        @NotNull
        String title,
        @NotNull
        String author,
        @NotNull
        @JsonProperty("isbn")
        @Size(min = 10, max = 17)
        String isbn,
        @NotNull
        @Min(0)
        BigDecimal price,
        @Size(min = 10, max = 1000)
        String description,
        @CoverImage
        String coverImage,
        @NotNull
        Set<Long> categories
) {}
