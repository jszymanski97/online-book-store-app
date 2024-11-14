package mate.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import mate.project.validation.CoverImage;

@Data
public class BookDtoWithoutCategoryIds {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 1, max = 255)
    private String title;
    @NotNull
    @Size(min = 1, max = 255)
    private String author;
    @NotNull
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @Size(min = 10, max = 1000)
    private String description;
    @CoverImage
    private String coverImage;
}
