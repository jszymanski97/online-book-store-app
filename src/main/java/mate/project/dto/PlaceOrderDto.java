package mate.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderDto {
    @NotNull
    private String shippingAddress;
}
