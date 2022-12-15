package saiga.payload.dto;

import saiga.model.Address;

import javax.validation.constraints.NotNull;

public record OrderDto(
        @NotNull(message = "Direction is required")
        Address fromAddress,
        Address toAddress
) {
}
