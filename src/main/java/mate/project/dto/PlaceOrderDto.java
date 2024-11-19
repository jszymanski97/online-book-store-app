package mate.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderDto {
    @NotBlank
    private String shippingAddress;
}
