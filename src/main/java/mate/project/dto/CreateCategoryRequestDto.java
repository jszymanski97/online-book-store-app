package mate.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotNull
        @Size(min = 8, max = 255)
        String name,
        @NotBlank
        @Size(min = 8, max = 255)
        String description
) {
}
