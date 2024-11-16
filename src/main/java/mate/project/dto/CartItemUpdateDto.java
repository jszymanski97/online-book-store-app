package mate.project.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartItemUpdateDto {
    @NotNull
    @Min(1)
    @Max(99)
    private int quantity;
}